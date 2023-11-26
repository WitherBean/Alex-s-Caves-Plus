package com.alexscavesplus.alexscavesplus.client.model;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EndKingModel extends GeoModel<EndKing> {
    @Override
    public ResourceLocation getModelResource(EndKing object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "geo/end_king.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EndKing object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/end_king.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EndKing animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "animations/end_king.animation.json");
    }
}