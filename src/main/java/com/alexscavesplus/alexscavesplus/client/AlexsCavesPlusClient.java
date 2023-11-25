package com.alexscavesplus.alexscavesplus.client;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.render.AjoltodonRenderer;
import com.alexscavesplus.alexscavesplus.client.render.GrottotaurusRenderer;
import com.alexscavesplus.alexscavesplus.client.render.LacandraeRenderer;
import com.alexscavesplus.alexscavesplus.client.render.VortexRenderer;
import com.alexscavesplus.alexscavesplus.common.entity.Grottotaurus;
import com.alexscavesplus.alexscavesplus.common.entity.Lacandrae;
import com.alexscavesplus.alexscavesplus.common.reg.ACPEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AlexsCavesPlusClient {

    @SubscribeEvent
    public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ACPEntityType.AJOLTODON.get(), AjoltodonRenderer::new);
        event.registerEntityRenderer(ACPEntityType.LACANDRAE.get(), LacandraeRenderer::new);
        event.registerEntityRenderer(ACPEntityType.GROTTOTAURUS.get(), GrottotaurusRenderer::new);
        event.registerEntityRenderer(ACPEntityType.VORTEX.get(), VortexRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
    }
}
