//package com.alexscavesplus.alexscavesplus.common.entity.ai.goals;
//
//import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
//import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.goal.Goal;
//import net.minecraft.world.entity.ai.util.DefaultRandomPos;
//import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.minecraft.world.phys.shapes.Shapes;
//
//import java.util.EnumSet;
//
//public class EndKingLeapGoal extends Goal {
//    private EndKing entity;
//    private BlockPos jumpTarget = null;
//    private boolean hasPreformedJump = false;
//
//    public EndKingLeapGoal(EndKing entity) {
//        this.entity = entity;
//        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
//    }
//
//    public boolean canUse() {
//        LivingEntity target = this.entity.getTarget();
//        if (this.entity.onGround() && this.entity.getRandom().nextInt(6) == 0) {
//            BlockPos findTarget = this.findJumpTarget();
//            if (findTarget != null) {
//                this.jumpTarget = findTarget;
//                return true;
//            }
//            if (target != null) {
//                this.entity.getLookControl().setLookAt(target);
//            }
//        }
//
//        return false;
//    }
//
//    private BlockPos findJumpTarget() {
//        Vec3 vec3 = DefaultRandomPos.getPos(this.entity, 25, 10);
//        if (vec3 != null) {
//            BlockPos blockpos = BlockPos.containing(vec3);
//            AABB aabb = this.entity.getBoundingBox().move(vec3.add(0.5, 1.0, 0.5).subtract(this.entity.position()));
//            if (this.entity.level().getBlockState(blockpos.below()).isSolidRender(this.entity.level(), blockpos.below()) && this.entity.getPathfindingMalus(WalkNodeEvaluator.getBlockPathTypeStatic(this.entity.level(), blockpos.mutable())) == 0.0F && this.entity.level().isUnobstructed(this.entity, Shapes.create(aabb))) {
//                return blockpos;
//            }
//        }
//
//        return null;
//    }
//
//    public void start() {
//        this.hasPreformedJump = false;
//        this.entity.getNavigation().stop();
//
//        this.entity.lookAt(Anchor.EYES, Vec3.atCenterOf(this.jumpTarget));
//    }
//
//    public boolean canContinueToUse() {
//        return (this.entity.isLeaping()) && this.jumpTarget != null;
//    }
//
//    public void tick() {
//        if (this.entity.isLeaping() && !this.hasPreformedJump) {
//            this.hasPreformedJump = true;
//            Vec3 vec3 = this.entity.getDeltaMovement();
//            Vec3 vec31 = new Vec3((double)((float)this.jumpTarget.getX() + 0.5F) - this.entity.getX(), 0.0, (double)((float)this.jumpTarget.getZ() + 0.5F) - this.entity.getZ());
//            if (vec31.length() > 100.0) {
//                vec31 = vec3.normalize().scale(100.0);
//            }
//
//            if (vec31.lengthSqr() > 1.0E-7) {
//                vec31 = vec31.scale(0.1550000011920929).add(vec3.scale(0.2));
//            }
//
//            this.entity.setDeltaMovement(vec31.x, 0.20000000298023224 + vec31.length() * 0.30000001192092896, vec31.z);
//        }
//
//    }
//}
