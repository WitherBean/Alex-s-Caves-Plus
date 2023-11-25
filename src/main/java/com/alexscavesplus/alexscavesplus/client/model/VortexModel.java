package com.alexscavesplus.alexscavesplus.client.model;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.Grottotaurus;
import com.alexscavesplus.alexscavesplus.common.entity.Vortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VortexModel extends GeoModel<Vortex> {
    @Override
    public ResourceLocation getModelResource(Vortex object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "geo/vortex.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Vortex object) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/vortex.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Vortex animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "animations/vortex.animation.json");
    }
}