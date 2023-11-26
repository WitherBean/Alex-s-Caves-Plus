package com.alexscavesplus.alexscavesplus.client.render;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.model.EndKingModel;
import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EndKingRenderer extends GeoEntityRenderer<EndKing> {
    public EndKingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndKingModel());
    }

    @Override
    public ResourceLocation getTextureLocation(EndKing animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/end_king.png");
    }

    @Override
    public void render(EndKing entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}