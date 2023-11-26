package com.alexscavesplus.alexscavesplus.common.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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

public class Grottotaurus extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public Grottotaurus(EntityType entityType, Level level) {
        super(entityType, level);
    }
    public boolean canBreatheUnderwater() {
        return true;
    }
    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 150.0f)
                .add(Attributes.ATTACK_KNOCKBACK, 1f)
                .add(Attributes.FOLLOW_RANGE, 20.0f)
                .add(Attributes.ATTACK_DAMAGE, 7.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.25f);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8, 5));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this, Lacandrae.class)).setAlertOthers());
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2, false));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 5, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 5, this::Attackpredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.grottotaurus.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.grottotaurus.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    private <T extends GeoAnimatable> PlayState Attackpredicate(AnimationState<T> tAnimationState) {
        if(this.swinging) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.grottotaurus.attack", Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
            }
            return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    public boolean isFood(ItemStack stack){
        return stack.is(ItemTags.FISHES);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return null;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }
}
