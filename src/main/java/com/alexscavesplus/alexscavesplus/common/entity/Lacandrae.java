package com.alexscavesplus.alexscavesplus.common.entity;

import com.alexscavesplus.alexscavesplus.common.reg.ACPSoundEvents;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class Lacandrae extends DinosaurEntity implements GeoEntity {
    public Lacandrae(EntityType entityType, Level level) {
        super(entityType, level);
    }
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Lacandrae.class)).setAlertOthers());
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>( this, Player.class, 16F, 0.8D, 1.6D));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
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
