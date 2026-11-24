package com.ivan21.infinitum.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Слот результата: при взятии забирает по одному предмету из каждой клетки сетки 9x9. */
public class ExtremeResultSlot extends Slot {
    private final ExtremeCraftingMenu menu;
    private final CraftingContainer craftSlots;

    public ExtremeResultSlot(ExtremeCraftingMenu menu, CraftingContainer craftSlots, Container result, int index, int x, int y) {
        super(result, index, x, y);
        this.menu = menu;
        this.craftSlots = craftSlots;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
        for (int i = 0; i < craftSlots.getContainerSize(); i++) {
            ItemStack in = craftSlots.getItem(i);
            if (in.isEmpty()) continue;
            ItemStack remainder = in.hasCraftingRemainingItem() ? in.getCraftingRemainingItem() : ItemStack.EMPTY;
            craftSlots.removeItem(i, 1);
            if (!remainder.isEmpty()) {
                if (craftSlots.getItem(i).isEmpty()) {
                    craftSlots.setItem(i, remainder);
                } else if (!player.getInventory().add(remainder)) {
                    player.drop(remainder, false);
                }
            }
        }
        menu.slotsChanged(craftSlots);
        super.onTake(player, stack);
    }
}
