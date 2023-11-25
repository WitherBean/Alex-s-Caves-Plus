package com.alexscavesplus.alexscavesplus.client.render;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.model.GrottotaurusModel;
import com.alexscavesplus.alexscavesplus.client.model.VortexModel;
import com.alexscavesplus.alexscavesplus.common.entity.Grottotaurus;
import com.alexscavesplus.alexscavesplus.common.entity.Vortex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VortexRenderer extends GeoEntityRenderer<Vortex> {
    public VortexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VortexModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Vortex animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/vortex.png");
    }

    @Override
    public void render(Vortex entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}