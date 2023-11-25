package com.alexscavesplus.alexscavesplus.client.model;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.EnderVirus;
import com.alexscavesplus.alexscavesplus.common.entity.Vortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EnderVirusModel extends GeoModel<EnderVirus> {
    @Override
    public ResourceLocation getModelResource(EnderVirus object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "geo/ender_virus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EnderVirus object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/ender_virus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EnderVirus animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "animations/ender_virus.animation.json");
    }
}