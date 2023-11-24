package com.alexscavesplus.alexscavesplus.common.reg;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AlexsCavesPlus.MODID);

    public static final RegistryObject<Item> MARSH_DISC = ITEMS.register("marsh_disc", () -> new RecordItem(16, ACPSoundEvents.MARSH_DISC, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 5520));
    public static final RegistryObject<ForgeSpawnEggItem> AJOLTODON_SPAWN_EGG = ITEMS.register("ajolotodon_spawn_egg",
            () -> new ForgeSpawnEggItem(ACPEntityType.AJOLTODON, 0xFF55AA, 0x27DA9F, new Item.Properties().stacksTo(64)));

    public static void register(IEventBus event){
        ITEMS.register(event);
    }

}
