package com.alexscavesplus.alexscavesplus.common.reg;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.*;
import com.alexscavesplus.alexscavesplus.common.entity.boss.EndKing;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ACPEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AlexsCavesPlus.MODID);

    public static final RegistryObject<EntityType<AjolotodonEntity>> AJOLTODON = ENTITY_TYPES.register("ajoltodon",
            () -> EntityType.Builder.of(AjolotodonEntity::new, MobCategory.AXOLOTLS)
                    .sized(0.85F, 0.75F).build(new ResourceLocation(AlexsCavesPlus.MODID, "ajoltodon").toString()));
    public static final RegistryObject<EntityType<Lacandrae>> LACANDRAE = ENTITY_TYPES.register("lacandrae",
            () -> EntityType.Builder.of(Lacandrae::new, MobCategory.AXOLOTLS)
                    .sized(1.2F, 0.6F).build(new ResourceLocation(AlexsCavesPlus.MODID, "lacandrae").toString()));
    public static final RegistryObject<EntityType<Grottotaurus>> GROTTOTAURUS = ENTITY_TYPES.register("grottotaurus",
            () -> EntityType.Builder.of(Grottotaurus::new, MobCategory.MONSTER)
                    .sized(3F, 3F).build(new ResourceLocation(AlexsCavesPlus.MODID, "grottotaurus").toString()));
    public static final RegistryObject<EntityType<Vortex>> VORTEX = ENTITY_TYPES.register("vortex",
            () -> EntityType.Builder.of(Vortex::new, MobCategory.MONSTER)
                    .sized(1.3F, 3.2F).build(new ResourceLocation(AlexsCavesPlus.MODID, "vortex").toString()));
    public static final RegistryObject<EntityType<EnderVirus>> ENDER_VIRUS = ENTITY_TYPES.register("ender_virus",
            () -> EntityType.Builder.of(EnderVirus::new, MobCategory.MONSTER)
                    .sized(2.5F, 2.5F).build(new ResourceLocation(AlexsCavesPlus.MODID, "ender_virus").toString()));
    public static final RegistryObject<EntityType<EndKing>> END_KING = ENTITY_TYPES.register("end_king",
            () -> EntityType.Builder.of(EndKing::new, MobCategory.MONSTER)
                    .sized(3F, 5F).build(new ResourceLocation(AlexsCavesPlus.MODID, "end_king").toString()));


    // Entity Type Tags
    public static final TagKey<EntityType<?>> AJOLOTODON_HUNTABLES = createTagKey("ajolotodon_huntables");
    public static final TagKey<EntityType<?>> AJOLOTODON_HUNT = createTagKey("ajolotodon_hunt");

    // For Entity Type Keys
    private static TagKey<EntityType<?>> createTagKey(String location){
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(AlexsCavesPlus.MODID, location));
    }

    // For Registering into AlexsCavesPlus.class
    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);}
}

