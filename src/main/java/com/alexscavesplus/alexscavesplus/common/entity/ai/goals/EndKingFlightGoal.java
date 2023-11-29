package com.alexscavesplus.alexscavesplus.common.entity.ai.goals;

import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class EndKingFlightGoal extends Goal {
    private EndKing entity;
    private double x;
    private double y;
    private double z;
    private boolean isFlying;

    public EndKingFlightGoal(EndKing subterranodon) {
        this.entity = subterranodon;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.entity.isVehicle() && (this.entity.getTarget() == null || !this.entity.getTarget().isAlive()) && !this.entity.isPassenger()) {
            boolean flag = false;
            if (this.entity.isPackFollower() && ((EndKing)this.entity.getPackLeader()).isFlying()) {
                this.isFlying = true;
                flag = true;
            }

            if (!flag) {
                if (this.entity.getRandom().nextInt(70) != 0 && !this.entity.isFlying()) {
                    return false;
                }

                if (this.entity.onGround()) {
                    this.isFlying = this.entity.getRandom().nextInt(3) == 0;
                } else {
                    this.isFlying = this.entity.getRandom().nextInt(8) > 0 && this.entity.timeFlying < 200;
                }
            }

            Vec3 target = this.getPosition();
            if (target == null) {
                return false;
            } else {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
                return true;
            }
        } else {
            return false;
        }
    }

    public void tick() {
        if (this.isFlying) {
            if (this.entity.resetFlightAIFlag || this.entity.horizontalCollision && this.entity.timeFlying % 10 == 0 || this.entity.distanceToSqr(this.x, this.y, this.z) < 9.0) {
                Vec3 target = this.getPosition();
                if (target != null) {
                    this.x = target.x;
                    this.y = target.y;
                    this.z = target.z;
                }

                this.entity.resetFlightAIFlag = false;
            }

            this.entity.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0);
        } else {
            if (this.entity.isFlying() || (this.entity).landingFlag) {
                this.entity.landingFlag = true;
            }

            this.entity.getNavigation().moveTo(this.x, this.y, this.z, 1.0);
        }

        if (!this.isFlying && this.entity.isFlying() && this.entity.onGround()) {
            this.entity.setFlying(false);
        }

        if (this.entity.isFlying() && this.entity.onGround() && this.entity.timeFlying > 40) {
            this.entity.setFlying(false);
        }

    }

    @Nullable
    protected Vec3 getPosition() {
        if (this.isOverWaterOrVoid()) {
            this.isFlying = true;
        }

        Vec3 vec3 = this.findOrFollowFlightPos();
        if (!this.isFlying) {
            return this.entity.isFlying() ? this.groundPosition(vec3).add(0.0, -1.0, 0.0) : LandRandomPos.getPos(this.entity, 10, 7);
        } else if ((this.entity.timeFlying < 200 || this.isOverWaterOrVoid())) {
            return vec3;
        } else {
            return this.entity.hasRestriction() && !this.entity.horizontalCollision ? Vec3.atCenterOf(this.entity.getRestrictCenter()) : this.groundPosition(vec3);
        }
    }
    private Vec3 findOrFollowFlightPos() {
        EndKing leader = this.entity;
        if (leader != this.entity && leader.lastFlightTargetPos != null) {
            int index = this.getPackPosition(this.entity, 0);
            int halfIndex = (int)Math.ceil((double)((float)index / 2.0F));
            float xOffset = 6.0F + this.entity.getRandom().nextFloat() * 2.0F;
            float zOffset = 4.0F + this.entity.getRandom().nextFloat() * 3.0F;
            Vec3 offset = (new Vec3((double)(((float)(index % 2) - 0.5F) * xOffset * (float)halfIndex), 0.0, (double)((float)halfIndex * zOffset))).yRot((float)Math.toRadians((double)(180.0F - leader.yBodyRot)));
            return leader.lastFlightTargetPos.add(offset);
        } else {
            Vec3 randOffsetMove = (new Vec3((double)(this.entity.getRandom().nextFloat() - 0.5F), (double)(this.entity.getRandom().nextFloat() - 0.5F), (double)(this.entity.getRandom().nextFloat() - 0.5F))).scale(2.0);
            return this.findFlightPos().add(randOffsetMove);
        }
    }

    private Vec3 findFlightPos() {
        Vec3 targetVec;
        float maxRot;
        float yRotOffset;
        Vec3 ground;
        float randCeilVal;
        if (this.entity.hasRestriction() && this.entity.getRestrictCenter() != null) {
            maxRot = 360.0F;
            ground = Vec3.atCenterOf(this.entity.getRestrictCenter());
            yRotOffset = (float)Math.toRadians((double)(this.entity.getRandom().nextFloat() * (maxRot - maxRot / 2.0F) * 0.5F));
            randCeilVal = (float)Math.toRadians((double)(this.entity.getRandom().nextFloat() * maxRot - maxRot / 2.0F));
            Vec3 distVec = (new Vec3(0.0, 0.0, (double)(15 + this.entity.getRandom().nextInt(15)))).xRot(yRotOffset).yRot(randCeilVal);
            targetVec = ground.add(distVec);
        } else {
            maxRot = this.entity.horizontalCollision ? 360.0F : 90.0F;
            float xRotOffset = (float)Math.toRadians((double)(this.entity.getRandom().nextFloat() * (maxRot - maxRot / 2.0F) * 0.5F));
            yRotOffset = (float)Math.toRadians((double)(this.entity.getRandom().nextFloat() * maxRot - maxRot / 2.0F));
            Vec3 lookVec = this.entity.getLookAngle().scale((double)(15 + this.entity.getRandom().nextInt(15))).xRot(xRotOffset).yRot(yRotOffset);
            targetVec = this.entity.position().add(lookVec);
        }

        if (!this.entity.level().isLoaded(BlockPos.containing(targetVec))) {
            return this.entity.position();
        } else {
            Vec3 heightAdjusted;
            if (this.entity.level().canSeeSky(BlockPos.containing(targetVec))) {
                ground = this.groundPosition(targetVec);
                heightAdjusted = new Vec3(targetVec.x, ground.y + 5.0 + (double)this.entity.getRandom().nextInt(10), targetVec.z);
            } else {
                ground = this.groundPosition(targetVec);

                BlockPos ceiling;
                for(ceiling = BlockPos.containing(ground).above(2); ceiling.getY() < this.entity.level().getMaxBuildHeight() && !this.entity.level().getBlockState(ceiling).isSolid(); ceiling = ceiling.above()) {
                }

                randCeilVal = 0.5F + this.entity.getRandom().nextFloat() * 0.2F;
                heightAdjusted = new Vec3(targetVec.x, ground.y + ((double)ceiling.getY() - ground.y) * (double)randCeilVal, targetVec.z);
            }

            BlockHitResult result = this.entity.level().clip(new ClipContext(this.entity.getEyePosition(), heightAdjusted, Block.COLLIDER, Fluid.NONE, this.entity));
            if (result.getType() == Type.MISS) {
                this.entity.lastFlightTargetPos = heightAdjusted;
                return heightAdjusted;
            } else {
                return result.getLocation();
            }
        }
    }

    private int getPackPosition(PackAnimal subterranodon, int index) {
        return index < 16 && subterranodon.getPriorPackMember() != null ? this.getPackPosition(subterranodon.getPriorPackMember(), index + 1) : index;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position;
        for(position = this.entity.blockPosition(); position.getY() > this.entity.level().getMinBuildHeight() && this.entity.level().isEmptyBlock(position) && this.entity.level().getFluidState(position).isEmpty(); position = position.below()) {
        }

        return !this.entity.level().getFluidState(position).isEmpty() || this.entity.level().getBlockState(position).is(Blocks.VINE) || position.getY() <= this.entity.level().getMinBuildHeight();
    }

    public Vec3 groundPosition(Vec3 airPosition) {
        BlockPos.MutableBlockPos ground = new BlockPos.MutableBlockPos();
        ground.set(airPosition.x, airPosition.y, airPosition.z);

        boolean flag;
        for(flag = false; ground.getY() < this.entity.level().getMaxBuildHeight() && !this.entity.level().getBlockState(ground).isSolid() && this.entity.level().getFluidState(ground).isEmpty(); flag = true) {
            ground.move(0, 1, 0);
        }

        ground.move(0, -1, 0);

        while(ground.getY() > this.entity.level().getMinBuildHeight() && !this.entity.level().getBlockState(ground).isSolid() && this.entity.level().getFluidState(ground).isEmpty()) {
            ground.move(0, -1, 0);
        }

        return Vec3.atCenterOf(flag ? ground.above() : ground.below());
    }

    public boolean canContinueToUse() {
        if (this.isFlying) {
            return this.entity.isFlying() && this.entity.distanceToSqr(this.x, this.y, this.z) > 5.0;
        } else {
            return !this.entity.getNavigation().isDone() && !this.entity.isVehicle();
        }
    }

    public void start() {
        if (this.isFlying) {
            this.entity.setFlying(true);
        } else {
            this.entity.getNavigation().moveTo(this.x, this.y, this.z, 1.0);
        }

    }

    public void stop() {
        this.entity.getNavigation().stop();
        this.entity.landingFlag = false;
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        super.stop();
    }
}
