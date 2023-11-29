package com.alexscavesplus.alexscavesplus.common.entity.boss;

import com.alexscavesplus.alexscavesplus.common.entity.ai.goals.EndKingFleeGoal;
import com.alexscavesplus.alexscavesplus.common.entity.ai.goals.EndKingFlightGoal;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import com.github.alexmodguy.alexscaves.server.message.MountedEntityKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class EndKing extends Monster implements PackAnimal, FlyingAnimal, GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Boolean> FLYING;
    private static final EntityDataAccessor<Boolean> HOVERING;
    private static final EntityDataAccessor<Float> METER_AMOUNT;
    private static final EntityDataAccessor<Integer> ATTACK_TICK;
    private int lSteps;
    private double lx;
    private double ly;
    private double lz;
    private double lyr;
    private double lxr;
    private double lxd;
    private double lyd;
    private double lzd;
    private float flyProgress;
    private float prevFlyProgress;
    private float flapAmount;
    private float prevFlapAmount;
    private float hoverProgress;
    private float prevHoverProgress;
    private float flightPitch = 0.0F;
    private float prevFlightPitch = 0.0F;
    private float flightRoll = 0.0F;
    private float prevFlightRoll = 0.0F;
    private float tailYaw;
    private float prevTailYaw;
    private boolean isLandNavigator;
    private EndKing priorPackMember;
    private EndKing afterPackMember;
    public int timeFlying;
    public Vec3 lastFlightTargetPos;
    public boolean resetFlightAIFlag = false;
    public boolean landingFlag;
    public boolean slowRidden;
    private int controlUpTicks = 0;
    private int controlDownTicks = 0;
    private AABB flightCollisionBox;
    private int timeVehicle;
    public float prevAttackProgress;
    public float attackProgress;
    private double lastStepX = 0.0;
    private double lastStepZ = 0.0;

    public EndKing(EntityType<? extends EndKing> type, Level level) {
        super(type, level);
        this.switchNavigator(true);
        this.tailYaw = this.yBodyRot;
        this.prevTailYaw = this.yBodyRot;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if(this.isFlying()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.end_king.fly", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if(this.isDescending()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.end_king.hover", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.end_king.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.end_king.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.addGoal(8, new EndKingFleeGoal(this));
        this.goalSelector.addGoal(0, new EndKingFlightGoal(this));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.timeFlying = compound.getInt("TimeFlying");
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("TimeFlying", this.timeFlying);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
        this.entityData.define(HOVERING, false);
        this.entityData.define(METER_AMOUNT, 1.0F);
        this.entityData.define(ATTACK_TICK, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 16.0)
                .add(Attributes.FLYING_SPEED, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.MAX_HEALTH, 890.0);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, this.level());
            this.isLandNavigator = true;
        } else {
            this.moveControl = new FlightMoveHelper(this);
            this.navigation = new FlyingPathNavigation(this, this.level());
            this.isLandNavigator = false;
        }

    }

    public void tick() {
        super.tick();
        this.prevFlyProgress = this.flyProgress;
        this.prevHoverProgress = this.hoverProgress;
        this.prevAttackProgress = this.attackProgress;
        this.prevFlapAmount = this.flapAmount;
        this.prevFlightPitch = this.flightPitch;
        this.prevFlightRoll = this.flightRoll;
        this.prevTailYaw = this.tailYaw;
        if (this.isFlying() && this.flyProgress < 5.0F) {
            ++this.flyProgress;
        }

        if (!this.isFlying() && this.flyProgress > 0.0F) {
            --this.flyProgress;
        }

        if (this.isHovering() && this.hoverProgress < 5.0F) {
            ++this.hoverProgress;
        }

        if (!this.isHovering() && this.hoverProgress > 0.0F) {
            --this.hoverProgress;
        }

        if (this.tickCount % 100 == 0 && this.getHealth() < this.getMaxHealth()) {
            this.heal(2.0F);
        }

        float yMov = (float)this.getDeltaMovement().y;
        if (!(yMov > 0.0F) && !this.isHovering()) {
            if (yMov <= 0.05F && this.flapAmount > 0.0F) {
                this.flapAmount -= 0.5F;
            }
        } else if (this.flapAmount < 5.0F) {
            ++this.flapAmount;
        }

        LivingEntity target;
        if (this.isFlying()) {
            if (this.timeFlying % 10 == 0 && (this.flapAmount > 0.0F || this.controlUpTicks > 0)) {
                this.playSound((SoundEvent)ACSoundRegistry.SUBTERRANODON_FLAP.get());
            }

            ++this.timeFlying;
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }

            if (this.getDeltaMovement().y < 0.0 && this.isAlive()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
            }

            if (this.isPassenger()) {
                this.setHovering(false);
                this.setFlying(false);
            }

            if (!this.level().isClientSide && this.onGround()) {
                target = this.getTarget();
                if (target != null && target.isAlive()) {
                    this.setHovering(false);
                    this.setFlying(false);
                }
            }
        } else {
            this.timeFlying = 0;
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }

        if (this.isVehicle() && !this.isBaby()) {
            this.setFlying(true);
            target = this.getControllingPassenger();
            if (target != null) {
                this.flightCollisionBox = this.getBoundingBox().expandTowards(0.0, (double)(-0.5F - target.getBbHeight()), 0.0);
                if (this.isRiderInWall()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.2, 0.0));
                }
            }
        }

        if (!this.level().isClientSide) {
            this.setHovering(this.isHoveringFromServer() && this.isFlying());
            if (this.isHovering() && this.isFlying() && this.isAlive() && !this.isVehicle()) {
                if (this.timeFlying < 30) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.075, 0.0));
                }

                if (this.landingFlag) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.3, 0.0));
                }
            }

            if (!this.isHovering() && this.isFlying() && this.timeFlying > 40 && this.onGround()) {
                this.setFlying(false);
            }
        } else {
            if (this.lSteps > 0) {
                double d5 = this.getX() + (this.lx - this.getX()) / (double)this.lSteps;
                double d6 = this.getY() + (this.ly - this.getY()) / (double)this.lSteps;
                double d7 = this.getZ() + (this.lz - this.getZ()) / (double)this.lSteps;
                this.setYRot(Mth.wrapDegrees((float)this.lyr));
                this.setXRot(this.getXRot() + (float)(this.lxr - (double)this.getXRot()) / (float)this.lSteps);
                --this.lSteps;
                this.setPos(d5, d6, d7);
            } else {
                this.reapplyPosition();
            }

            Player player = AlexsCaves.PROXY.getClientSidePlayer();
            if (player != null && player.isPassengerOfSameVehicle(this)) {
                if (AlexsCaves.PROXY.isKeyDown(0) && !AlexsCaves.PROXY.isKeyDown(1) && this.controlUpTicks < 2 && this.getMeterAmount() > 0.1F && this.getMeterAmount() > 0.1F) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.getId(), player.getId(), 0));
                    this.controlUpTicks = 5;
                }

                if (AlexsCaves.PROXY.isKeyDown(1) && !AlexsCaves.PROXY.isKeyDown(0) && this.controlDownTicks < 2) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.getId(), player.getId(), 1));
                    this.controlDownTicks = 5;
                }
            }
        }

        if (this.controlDownTicks > 0) {
            --this.controlDownTicks;
        } else if (this.controlUpTicks > 0) {
            --this.controlUpTicks;
        }

        if (this.isVehicle()) {
            ++this.timeVehicle;
        } else {
            this.timeVehicle = 0;
        }

        if (this.getMeterAmount() < 1.0F && this.controlUpTicks == 0) {
            this.setMeterAmount(this.getMeterAmount() + (this.slowRidden ? 0.002F : 0.001F));
        }

        if ((Integer)this.entityData.get(ATTACK_TICK) > 0) {
            this.entityData.set(ATTACK_TICK, (Integer)this.entityData.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                ++this.attackProgress;
            }
        } else {
            target = this.getTarget();
            if (this.attackProgress == 5.0F && target != null && (double)this.distanceTo(target) < 3.0 + (double)target.getBbWidth() && this.hasLineOfSight(target)) {
                target.hurt(this.damageSources().mobAttack(this), (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                this.playSound((SoundEvent)ACSoundRegistry.SUBTERRANODON_ATTACK.get());
            }

            if (this.attackProgress > 0.0F) {
                --this.attackProgress;
            }
        }

        this.tickRotation(Mth.clamp(yMov, -1.0F, 1.0F) * -57.295776F);
        this.lastStepX = this.xo;
        this.lastStepZ = this.zo;
    }

    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double)yr;
        this.lxr = (double)xr;
        this.lSteps = steps;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }

    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }

    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        float f = player.zza < 0.0F ? 0.5F : 1.0F;
        return new Vec3((double)(player.xxa * 0.25F), this.controlUpTicks > 0 ? 1.0 : (this.controlDownTicks > 0 ? -1.0 : 0.0), (double)(player.zza * 0.5F * f));
    }

    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        this.slowRidden = player.zza < 0.3F || this.timeVehicle < 10 || this.onGround();
        if (player.zza != 0.0F || player.xxa != 0.0F) {
            this.setRot(player.getYRot(), player.getXRot() * 0.25F);
            this.setTarget((LivingEntity)null);
        }

    }

    protected float getFlyingSpeed() {
        return this.getSpeed();
    }

    protected float getRiddenSpeed(Player rider) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    public boolean isRiderInWall() {
        Entity rider = this.getControllingPassenger();
        if (rider != null && !rider.noPhysics) {
            float f = rider.getDimensions(Pose.STANDING).width * 0.8F;
            AABB aabb = AABB.ofSize(rider.position().add(0.0, 0.5, 0.0), (double)f, 1.0E-6, (double)f);
            return BlockPos.betweenClosedStream(aabb).anyMatch((state) -> {
                BlockState blockstate = this.level().getBlockState(state);
                return !blockstate.isAir() && blockstate.isSuffocating(this.level(), state) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), state).move((double)state.getX(), (double)state.getY(), (double)state.getZ()), Shapes.create(aabb), BooleanOp.AND);
            });
        } else {
            return false;
        }
    }

    public boolean doHurtTarget(Entity entityIn) {
        this.entityData.set(ATTACK_TICK, 7);
        return true;
    }

    private boolean isHoveringFromServer() {
        if (this.isVehicle()) {
            return this.slowRidden;
        } else {
            return this.landingFlag || this.timeFlying < 30;
        }
    }

    private void tickRotation(float yMov) {
        this.flightPitch = Mth.approachDegrees(this.flightPitch, yMov, 10.0F);
        float threshold = 1.0F;
        boolean flag = false;
        if (this.isFlying() && this.yRotO - this.getYRot() > threshold) {
            this.flightRoll += 10.0F;
            flag = true;
        }

        if (this.isFlying() && this.yRotO - this.getYRot() < -threshold) {
            this.flightRoll -= 10.0F;
            flag = true;
        }

        if (!flag) {
            if (this.flightRoll > 0.0F) {
                this.flightRoll = Math.max(this.flightRoll - 5.0F, 0.0F);
            }

            if (this.flightRoll < 0.0F) {
                this.flightRoll = Math.min(this.flightRoll + 5.0F, 0.0F);
            }
        }

        this.flightRoll = Mth.clamp(this.flightRoll, -60.0F, 60.0F);
        this.tailYaw = Mth.approachDegrees(this.tailYaw, this.yBodyRot, 8.0F);
    }

    public boolean isFlying() {
        return (Boolean)this.entityData.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (flying && this.isBaby()) {
            flying = false;
        }

        this.entityData.set(FLYING, flying);
    }

    public boolean isHovering() {
        return (Boolean)this.entityData.get(HOVERING);
    }

    public void setHovering(boolean flying) {
        if (flying && this.isBaby()) {
            flying = false;
        }

        this.entityData.set(HOVERING, flying);
    }

    public boolean hasRidingMeter() {
        return true;
    }

    public float getMeterAmount() {
        return (Float)this.entityData.get(METER_AMOUNT);
    }

    public void setMeterAmount(float flightPower) {
        this.entityData.set(METER_AMOUNT, flightPower);
    }

    public float getFlapAmount(float partialTick) {
        return (this.prevFlapAmount + (this.flapAmount - this.prevFlapAmount) * partialTick) * 0.2F;
    }

    public float getFlyProgress(float partialTick) {
        return (this.prevFlyProgress + (this.flyProgress - this.prevFlyProgress) * partialTick) * 0.2F;
    }

    public float getHoverProgress(float partialTick) {
        return (this.prevHoverProgress + (this.hoverProgress - this.prevHoverProgress) * partialTick) * 0.2F;
    }

    public float getBiteProgress(float partialTick) {
        return (this.prevAttackProgress + (this.attackProgress - this.prevAttackProgress) * partialTick) * 0.2F;
    }

    public float getFlightPitch(float partialTick) {
        return this.prevFlightPitch + (this.flightPitch - this.prevFlightPitch) * partialTick;
    }

    public float getFlightRoll(float partialTick) {
        return this.prevFlightRoll + (this.flightRoll - this.prevFlightRoll) * partialTick;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void resetPackFlags() {
        this.resetFlightAIFlag = true;
    }

    public PackAnimal getPriorPackMember() {
        return this.priorPackMember;
    }

    public PackAnimal getAfterPackMember() {
        return this.afterPackMember;
    }

    @Override
    public void setPriorPackMember(PackAnimal packAnimal) {

    }

    @Override
    public void setAfterPackMember(PackAnimal packAnimal) {

    }

    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3.0, 3.0, 3.0);
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_DEATH;
    }

    static {
        FLYING = SynchedEntityData.defineId(EndKing.class, EntityDataSerializers.BOOLEAN);
        HOVERING = SynchedEntityData.defineId(EndKing.class, EntityDataSerializers.BOOLEAN);
        METER_AMOUNT = SynchedEntityData.defineId(EndKing.class, EntityDataSerializers.FLOAT);
        ATTACK_TICK = SynchedEntityData.defineId(EndKing.class, EntityDataSerializers.INT);
    }

    class FlightMoveHelper extends MoveControl {
        private final EndKing parentEntity;

        public FlightMoveHelper(EndKing bird) {
            super(bird);
            this.parentEntity = bird;
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.wantedX - this.parentEntity.getX(), this.wantedY - this.parentEntity.getY(), this.wantedZ - this.parentEntity.getZ());
                double d5 = vector3d.length();
                if (d5 < this.parentEntity.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    this.parentEntity.setDeltaMovement(this.parentEntity.getDeltaMovement().scale(0.5));
                } else {
                    float hoverSlow = this.parentEntity.isHoveringFromServer() && !this.parentEntity.landingFlag ? 0.2F : 1.0F;
                    this.parentEntity.setDeltaMovement(this.parentEntity.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.1 / d5).multiply((double)hoverSlow, 1.0, (double)hoverSlow)));
                    Vec3 vector3d1 = this.parentEntity.getDeltaMovement();
                    float f = -((float)Mth.atan2(vector3d1.x, vector3d1.z)) * 180.0F / 3.1415927F;
                    this.parentEntity.setYRot(Mth.approachDegrees(this.parentEntity.getYRot(), f, 20.0F));
                    this.parentEntity.yBodyRot = this.parentEntity.getYRot();
                }
            }

        }
    }
}