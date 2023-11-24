package com.alexscavesplus.alexscavesplus.client.model;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.Lacandrae;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LacandraeModel extends GeoModel<Lacandrae> {
    @Override
    public ResourceLocation getModelResource(Lacandrae object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "geo/lacandrae.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lacandrae object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/lacandrae.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Lacandrae animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "animations/lacandrae.animation.json");
    }
}