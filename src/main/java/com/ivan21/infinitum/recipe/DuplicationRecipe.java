package com.ivan21.infinitum.recipe;

import com.ivan21.infinitum.item.DuplicationCrystalItem;
import com.ivan21.infinitum.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Рецепт дюпа в обычном верстаке: кристалл дюпа + любой стак = копия этого стака.
 * Кристалл и исходный предмет возвращаются в сетку.
 */
public class DuplicationRecipe extends CustomRecipe {
    public DuplicationRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    private static boolean isCrystal(ItemStack stack) {
        return stack.getItem() instanceof DuplicationCrystalItem;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int crystals = 0;
        int others = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (s.isEmpty()) continue;
            if (isCrystal(s)) crystals++;
            else others++;
        }
        return crystals == 1 && others == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (!s.isEmpty() && !isCrystal(s)) {
                return s.copy(); // копия всего стака, с NBT и зачарованиями
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            ItemStack s = inv.getItem(i);
            if (!s.isEmpty()) {
                ItemStack keep = s.copy();
                keep.setCount(1); // вернуть тот 1 предмет, что съел верстак
                list.set(i, keep);
            }
        }
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DUPLICATION.get();
    }
}
