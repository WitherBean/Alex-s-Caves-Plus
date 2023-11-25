package com.alexscavesplus.alexscavesplus.common.entity;

import com.alexscavesplus.alexscavesplus.client.events.CameraShakeEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class Vortex extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public Vortex(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 45.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5f)
                .add(Attributes.FOLLOW_RANGE, 20.0f)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8, 5));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this, Lacandrae.class)).setAlertOthers());
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, false));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>( this, Player.class, 4F, 0.8D, 1.6D));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.vortex.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }
    public void baseTick() {
        super.baseTick();
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(1.25))) {
            if (!(entity instanceof Vortex)) {
                if (this.distanceTo(entity) > 2.5) {
                    double d0 = entity.getX() - this.getX();
                    double d1 = entity.getZ() - this.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                    entity.push(d0 / d2 * 0.5D, 0.5D, d1 / d2 * 0.5D);
                    CameraShakeEvent.shake(this.level(), 20, 0.05F, this.blockPosition(), 24);

                }
            }
        }
    }
    public boolean isSensitiveToWater() {
        return true;
    }
}
