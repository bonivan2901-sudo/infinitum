package com.ivan21.infinitum.item;

import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public final class ModTiers {
    /** Неразрушимый уровень инструментов (0 прочности = предмет не ломается). */
    public static final Tier INFINITY = new Tier() {
        @Override public int getUses() { return 0; }
        @Override public float getSpeed() { return 9999f; }
        @Override public float getAttackDamageBonus() { return 0f; }
        @Override public int getLevel() { return 10; }
        @Override public int getEnchantmentValue() { return 30; }
        @Override public Ingredient getRepairIngredient() { return Ingredient.of(ModItems.INFINITY_INGOT.get()); }
    };

    private ModTiers() {}
}
