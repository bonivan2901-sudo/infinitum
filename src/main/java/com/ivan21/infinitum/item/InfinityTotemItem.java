package com.ivan21.infinitum.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Тотем Бесконечности: спасает от смерти из любого слота инвентаря и не тратится (логика в ModEvents). */
public class InfinityTotemItem extends Item {
    public InfinityTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.infinitum.totem.1").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.infinitum.totem.2").withStyle(ChatFormatting.GRAY));
    }
}
