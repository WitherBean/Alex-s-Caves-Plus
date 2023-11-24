package com.alexscavesplus.alexscavesplus.client.render;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.model.LacandraeModel;
import com.alexscavesplus.alexscavesplus.common.entity.Lacandrae;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LacandraeRenderer extends GeoEntityRenderer<Lacandrae> {
    public LacandraeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LacandraeModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Lacandrae animatable) {
        return new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/lacandrae.png");
    }

    @Override
    public void render(Lacandrae entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}