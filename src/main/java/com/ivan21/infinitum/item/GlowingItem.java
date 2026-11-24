package com.ivan21.infinitum.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Предмет с эффектом зачарования (сингулярности, катализатор, слитки). */
public class GlowingItem extends Item {
    public GlowingItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
