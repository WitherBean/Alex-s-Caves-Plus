package com.alexscavesplus.alexscavesplus.common.entity;

import com.alexscavesplus.alexscavesplus.common.reg.ACPEntityType;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;

public class Lacandrae extends DinosaurEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public Lacandrae(EntityType<? extends Lacandrae> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setMaxUpStep(1.0F);
        ResourceLocation[] textures = new ResourceLocation[]{
                new ResourceLocation("alexscavesplus", "textures/entity/lacandre.png"),
                new ResourceLocation("alexscavesplus", "textures/entity/lacandre2.png"),
                new ResourceLocation("alexscavesplus", "textures/entity/lacandre4.png"),
                new ResourceLocation("alexscavesplus", "textures/entity/lacandre3.png")
        };
        this.texture = textures[new Random().nextInt(textures.length)];
    }
    public ResourceLocation getTexture() {
        return this.texture;
    }
    private final ResourceLocation texture;
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public BlockState createEggBlockState() {
        return null;
    }
    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 27.0f)
                .add(Attributes.FOLLOW_RANGE, 20.0f)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 1.0f);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 8, 5));
        this.goalSelector.addGoal(0, new RandomStrollGoal(this, 0.25, 5));
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 2, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Lacandrae.class)).setAlertOthers());
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>( this, Player.class, 16F, 0.8D, 1.6D));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ACPEntityType.AJOLTODON.get().create(pLevel);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lacandrae.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isInWater()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lacandrae.swim", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.lacandrae.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
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

    protected @NotNull SoundEvent getSwimSplashSound() {
        return SoundEvents.AXOLOTL_SPLASH;
    }

    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }
}
