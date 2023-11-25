package com.alexscavesplus.alexscavesplus.client.render;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.model.EnderVirusModel;
import com.alexscavesplus.alexscavesplus.client.model.VortexModel;
import com.alexscavesplus.alexscavesplus.common.entity.EnderVirus;
import com.alexscavesplus.alexscavesplus.common.entity.Vortex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EnderVirusRenderer extends GeoEntityRenderer<EnderVirus> {
    public EnderVirusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EnderVirusModel());
    }

    @Override
    public ResourceLocation getTextureLocation(EnderVirus animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/ender_virus.png");
    }

    @Override
    public void render(EnderVirus entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}