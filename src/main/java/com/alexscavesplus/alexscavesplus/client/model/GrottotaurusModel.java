package com.alexscavesplus.alexscavesplus.client.model;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.Grottotaurus;
import com.alexscavesplus.alexscavesplus.common.entity.Lacandrae;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GrottotaurusModel extends GeoModel<Grottotaurus> {
    @Override
    public ResourceLocation getModelResource(Grottotaurus object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "geo/grottotaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Grottotaurus object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/grottotaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Grottotaurus animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "animations/grottotaurus.animation.json");
    }
}