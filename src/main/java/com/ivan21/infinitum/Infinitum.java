package com.ivan21.infinitum;

import com.ivan21.infinitum.registry.ModBlockEntities;
import com.ivan21.infinitum.registry.ModBlocks;
import com.ivan21.infinitum.registry.ModCreativeTab;
import com.ivan21.infinitum.registry.ModEntities;
import com.ivan21.infinitum.registry.ModEntities;
import com.ivan21.infinitum.registry.ModItems;
import com.ivan21.infinitum.registry.ModMenus;
import com.ivan21.infinitum.registry.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Infinitum.MODID)
public class Infinitum {
    public static final String MODID = "infinitum";

    public Infinitum() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModMenus.MENUS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);
        ModRecipes.TYPES.register(bus);
        ModCreativeTab.TABS.register(bus);
    }
}
