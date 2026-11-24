package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.menu.ExtremeCraftingMenu;
import com.ivan21.infinitum.Infinitum;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Infinitum.MODID);

    public static final RegistryObject<MenuType<ExtremeCraftingMenu>> EXTREME_CRAFTING = MENUS.register("extreme_crafting",
            () -> IForgeMenuType.create((windowId, inv, buf) -> new ExtremeCraftingMenu(windowId, inv)));

    private ModMenus() {}
}
