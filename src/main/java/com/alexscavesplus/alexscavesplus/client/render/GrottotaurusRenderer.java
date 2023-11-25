package com.alexscavesplus.alexscavesplus.client.render;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.model.GrottotaurusModel;
import com.alexscavesplus.alexscavesplus.client.model.LacandraeModel;
import com.alexscavesplus.alexscavesplus.common.entity.Grottotaurus;
import com.alexscavesplus.alexscavesplus.common.entity.Lacandrae;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GrottotaurusRenderer extends GeoEntityRenderer<Grottotaurus> {
    public GrottotaurusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GrottotaurusModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Grottotaurus animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/grottotaurus.png");
    }

    @Override
    public void render(Grottotaurus entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}