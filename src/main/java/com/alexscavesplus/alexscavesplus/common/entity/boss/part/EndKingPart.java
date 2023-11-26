package com.alexscavesplus.alexscavesplus.common.entity.boss.part;

import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

public class EndKingPart extends PartEntity<EndKing> {

    public final EndKing parentMob;

    public EndKingPart(EndKing parent) {
        super(parent);
        this.parentMob = parent;
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public boolean hurt(DamageSource p_19946_, float p_19947_) {
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this.parentMob, (byte) 9);
            this.parentMob.hurt(p_19946_, p_19947_);
        }
        return true;
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public EntityDimensions getDimensions(Pose p_213305_1_) {
        return EntityDimensions.scalable(5F, 5F);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }
}
