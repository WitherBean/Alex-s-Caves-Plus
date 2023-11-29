package com.alexscavesplus.alexscavesplus.common.entity.boss;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Comparator;
import java.util.List;

public class EndKingg extends Mob implements GeoEntity, Enemy {

    protected float getSoundVolume() {
        return 5.0F;
    }
//    private final EndKingPart[] subEntities;
//    private final EndKingPartWing[] subEntitiesWing;
//    public EndKingPart subEntity;
//    public EndKingPart subEntity2;
//    public EndKingPart subEntity3;
//    public EndKingPart subEntity4;
//    public EndKingPart subEntity5;
//    public EndKingPartWing wingRight;
//    public EndKingPartWing wingLeft;
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public float getStepHeight() {
        return 1;
    }
//    @Override
//    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
//        return new PartEntity<?>[]{this.subEntity, this.subEntity2, this.subEntity3, this.subEntity4, this.subEntity5, this.wingRight, this.wingLeft};
//    }
    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
    }
    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
    }
//    public ItemEntity spawnAtLocation(ItemStack stack) {
//        ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY() + 1.0, this.getZ(), stack);
//        if (itementity != null) {
//            if (this.subEntity5 != null) {
//                Vec3 yOnlyViewVector = new Vec3(this.getViewVector(1.0F).x, 0.0, this.getViewVector(1.0F).z);
//                Vec3 mouth = this.subEntity5.position().add(yOnlyViewVector.scale(-0.5)).add(0.0, 0.5, 0.0);
//                itementity.setPos(mouth);
//                itementity.setDeltaMovement(yOnlyViewVector.add((double)(this.random.nextFloat() * 0.2F - 0.1F), (double)(this.random.nextFloat() * 0.2F - 0.1F), (double)(this.random.nextFloat() * 0.2F - 0.1F)).normalize().scale((double)(0.8F + this.level().random.nextFloat() * 0.3F)));
//            }
//
//            itementity.setGlowingTag(true);
//            itementity.setDefaultPickUpDelay();
//        }
//
//        this.level().addFreshEntity(itementity);
//        return itementity;
//    }

    public EndKingg(EntityType entityType, Level level) {
        super(entityType, level);
//        this.subEntity = new EndKingPart(this, this.subEntity2);
//        this.subEntity2 = new EndKingPart(this, this.subEntity3);
//        this.subEntity3 = new EndKingPart(this, this.subEntity2);
//        this.subEntity4 = new EndKingPart(this, subEntity3);
//        this.subEntity5 = new EndKingPart(this, this.subEntity);
//        this.wingRight = new EndKingPartWing(this, this.subEntity2);
//        this.wingLeft = new EndKingPartWing(this, this.subEntity2);
        this.moveControl = new FlyingMoveControl(this, 10, false);
//        this.subEntities = new EndKingPart[]{this.subEntity5, this.subEntity4, this.subEntity, this.subEntity2, this.subEntity3};
//        this.subEntitiesWing = new EndKingPartWing[]{this.wingRight, this.wingLeft};
//        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
//        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntitiesWing.length + 1) + 1);
    }

//    protected PathNavigation createNavigation(Level pLevel) {
//        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
//        flyingpathnavigation.setCanOpenDoors(false);
//        flyingpathnavigation.setCanFloat(true);
//        flyingpathnavigation.setCanPassDoors(true);
//        return flyingpathnavigation;
//    }
    public boolean shouldRenderAtSqrDistance(double p_33107_) {
        return true;
    }
    static enum AttackPhase {
        CIRCLE,
        CHARGE,
        SWOOP;
    }

//    public boolean hurt(DamageSource p_35055_, float p_35056_) {
//        boolean flag = super.hurt(p_35055_, p_35056_);
//        if (this.level().isClientSide) {
//            return false;
//        } else {
//            return false;
//        }
//    }
    protected void registerGoals() {
    }
//    protected void updatePart() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 0, 0, 40 / 16.0F, this.yBodyRot);
//        this.movePart(this.subEntity, partPos.x, partPos.y, partPos.z);
//    }
//    protected void updatePart3() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 0, 0, 80 / 32.0F, this.yBodyRot);
//        this.movePart(this.subEntity3, partPos.x, partPos.y, partPos.z);
//    }
//    protected void updatePart2() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 0, 0, 80 / -16.0F, this.yBodyRot);
//        this.movePart(this.subEntity2, partPos.x, partPos.y, partPos.z);
//    }
//    protected void updatePart4() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 0, 0, 160 / -16.0F, this.yBodyRot);
//        this.movePart(this.subEntity4, partPos.x, partPos.y, partPos.z);
//    }
//    protected void updatePart5() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 0, 0, 160 / 16.0F, this.yBodyRot);
//        this.movePart(this.subEntity5, partPos.x, partPos.y, partPos.z);
//    }
//    protected void updatePartWingRight() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 100 / 16.0F, 5, 0, this.yBodyRot);
//        this.movePartWing(this.wingRight, partPos.x, partPos.y, partPos.z);
//    }
//    protected void updatePartWingLeft() {
//        Vec3 partPos = PositionUtils.getOffsetPos(this, 100 / -16.0F, 5, 0, this.yBodyRot);
//        this.movePartWing(this.wingLeft, partPos.x, partPos.y, partPos.z);
//    }
    public boolean causeFallDamage(float p_149589_, float p_149590_, DamageSource p_149591_) {
        return false;
    }
//    @Override
//    public boolean isAttackable() {
//        return false;
//    }

//    @Override
//    public void setId(int p_145769_1_) {
//        super.setId(p_145769_1_);
//        this.subEntity.setId(p_145769_1_ + 1);
//        this.subEntity2.setId(p_145769_1_ + 2);
//        this.subEntity3.setId(p_145769_1_ + 3);
//        this.subEntity4.setId(p_145769_1_ + 4);
//        this.subEntity5.setId(p_145769_1_ + 5);
//        this.wingRight.setId(p_145769_1_ + 6);
//        this.wingLeft.setId(p_145769_1_ + 7);
//    }

//    protected void movePart(EndKingPart part, double dX, double dY, double dZ) {
//        Vec3 lastPos = new Vec3(part.getX(), part.getY(), part.getZ());
//
//        part.setPos(dX, dY, dZ);
//
//        part.xo = lastPos.x;
//        part.yo = lastPos.y;
//        part.zo = lastPos.z;
//        part.xOld = lastPos.x;
//        part.yOld = lastPos.y;
//        part.zOld = lastPos.z;
//    }
//    protected void movePartWing(EndKingPartWing part, double dX, double dY, double dZ) {
//        Vec3 lastPos = new Vec3(part.getX(), part.getY(), part.getZ());
//
//        part.setPos(dX, dY, dZ);
//
//        part.xo = lastPos.x;
//        part.yo = lastPos.y;
//        part.zo = lastPos.z;
//        part.xOld = lastPos.x;
//        part.yOld = lastPos.y;
//        part.zOld = lastPos.z;
//    }
//
//    @Override
//    public boolean isMultipartEntity() {
//        return true;
//    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 800.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5f)
                .add(Attributes.ARMOR, 12f)
                .add(Attributes.ARMOR_TOUGHNESS, 1.5f)
                .add(Attributes.FOLLOW_RANGE, 200.0f)
                .add(Attributes.ATTACK_DAMAGE, 15f)
                .add(Attributes.FLYING_SPEED, 0.3f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f);
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }

    }
    public boolean isPickable() {
        return true;
    }

    public void setCustomName(@javax.annotation.Nullable Component pName) {
        super.setCustomName(pName);
        this.bossEvent.setName(this.getDisplayName());
    }
    public void aiStep() {
        if (this.tickCount % 20 == 0) {
            this.heal(1.0F);
        }

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
//        this.updatePart();
//        this.updatePart2();
//        this.updatePart3();
//        this.updatePart4();
//        this.updatePart5();
//        this.updatePartWingRight();
//        this.updatePartWingLeft();
//        this.yBodyRot = this.getYRot();
//        Vec3[] avec3 = new Vec3[this.subEntities.length];
//
//        for(int j = 0; j < this.subEntities.length; ++j) {
//            avec3[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
//        }
    }
    public boolean canChangeDimensions() {
        return false;
    }
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);

    }
//    public EndKingPart[] getSubEntities() {
//        return this.subEntities;
//    }
//    public EndKingPartWing[] getSubEntitiesWing() {
//        return this.subEntitiesWing;
//    }
    private Path reconstructPath(Node pStart, Node pFinish) {
        List<Node> list = Lists.newArrayList();
        Node node = pFinish;
        list.add(0, pFinish);

        while(node.cameFrom != null) {
            node = node.cameFrom;
            list.add(0, node);
        }

        return new Path(list, new BlockPos(pFinish.x, pFinish.y, pFinish.z), true);
    }
//    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
//        super.recreateFromPacket(pPacket);
//        if (true) return;
//        EndKingPart[] aenderdragonpart = this.getSubEntities();
//        EndKingPartWing[] aenderdragonpartwing = this.getSubEntitiesWing();
//
//        for(int i = 0; i < aenderdragonpart.length; ++i) {
//            aenderdragonpart[i].setId(i + pPacket.getId());
//        }
//        for(int i = 0; i < aenderdragonpartwing.length; ++i) {
//            aenderdragonpart[i].setId(i + pPacket.getId());
//        }
//
//    }

    protected boolean canRide(Entity pEntity) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
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

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_DEATH;
    }
    class EndKingAttackLivingTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
        private int nextScanTick = reducedTickDelay(20);

        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                List<Player> list = EndKingg.this.level().getNearbyPlayers(this.attackTargeting, EndKingg.this, EndKingg.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(LivingEntity player : list) {
                        if (EndKingg.this.canAttack(player, TargetingConditions.DEFAULT)) {
                            EndKingg.this.setTarget(player);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = EndKingg.this.getTarget();
            return livingentity != null ? EndKingg.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }
    }
}