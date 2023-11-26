package com.alexscavesplus.alexscavesplus.common.entity.boss;

import com.alexscavesplus.alexscavesplus.client.events.CameraShakeEvent;
import com.alexscavesplus.alexscavesplus.common.entity.boss.part.EndKingPart;
import com.alexscavesplus.alexscavesplus.utils.PositionUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Random;

public class EndKing extends Monster implements GeoEntity {

    public EndKingPart subEntity;
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public float getStepHeight() {
        return 1;
    }
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return new PartEntity<?>[]{this.subEntity};
    }

    public EndKing(EntityType entityType, Level level) {
        super(entityType, level);
        this.subEntity = new EndKingPart(this);
    }
    public boolean hurt(DamageSource p_35055_, float p_35056_) {
        boolean flag = super.hurt(p_35055_, p_35056_);
        if (this.level().isClientSide) {
            return false;
        } else {
            return flag;
        }
    }
    protected void updatePart() {
        Vec3 partPos = PositionUtils.getOffsetPos(this, 0, -1, 40 / 16.0F, this.yBodyRot);
        this.movePart(this.subEntity, partPos.x, partPos.y, partPos.z);
    }
    @Override
    public void setId(int p_145769_1_) {
        super.setId(p_145769_1_);
        this.subEntity.setId(p_145769_1_ + 1);
    }

    protected void movePart(EndKingPart part, double dX, double dY, double dZ) {
        Vec3 lastPos = new Vec3(part.getX(), part.getY(), part.getZ());

        part.setPos(dX, dY, dZ);

        part.xo = lastPos.x;
        part.yo = lastPos.y;
        part.zo = lastPos.z;
        part.xOld = lastPos.x;
        part.yOld = lastPos.y;
        part.zOld = lastPos.z;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

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
                .add(Attributes.MOVEMENT_SPEED, 0.278f);
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }

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
        this.updatePart();
    }
    public boolean canChangeDimensions() {
        return false;
    }

    protected boolean canRide(Entity pEntity) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8, 5));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, false));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 10, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 10, this::Attackpredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    private <T extends GeoAnimatable> PlayState Attackpredicate(AnimationState<T> tAnimationState) {
        if(this.swinging) {
            Random random = new Random();
            int attackAnim = random.nextInt(2);
            switch(attackAnim) {
                case 0:
                    tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.attack", Animation.LoopType.PLAY_ONCE));
                    this.swinging = false;
                    break;
                case 1:
                    tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.attack2", Animation.LoopType.PLAY_ONCE));
                    this.swinging = false;
                    break;
            }
        }
        return PlayState.CONTINUE;
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
}
