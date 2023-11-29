package com.alexscavesplus.alexscavesplus.common.entity.ai.goals;

import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

public class EndKingFleeGoal extends Goal {
    private EndKing subterranodon;

    public EndKingFleeGoal(EndKing subterranodon) {
        this.subterranodon = subterranodon;
    }

    public boolean canUse() {
        if (!this.subterranodon.isFlying()) {
            long worldTime = this.subterranodon.level().getGameTime() % 10L;
            if (this.subterranodon.getRandom().nextInt(10) != 0 && worldTime != 0L) {
                return false;
            } else {
                AABB aabb = this.subterranodon.getBoundingBox().inflate(7.0);
                List<Entity> list = this.subterranodon.level().getEntitiesOfClass(Entity.class, aabb, (entity) -> {
                    return entity.getType().is(ACTagRegistry.SUBTERRANODON_FLEES);
                });
                return !list.isEmpty();
            }
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void start() {
        this.subterranodon.setFlying(true);
        this.subterranodon.setHovering(true);
    }
}
