package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.Infinitum;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Infinitum.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.infinitum"))
            .icon(() -> new ItemStack(ModItems.INFINITY_INGOT.get()))
            .displayItems((params, output) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
            .build());

    private ModCreativeTab() {}
}
