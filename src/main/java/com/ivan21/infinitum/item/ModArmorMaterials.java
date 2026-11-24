package com.ivan21.infinitum.item;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public final class ModArmorMaterials {
    public static final ArmorMaterial INFINITY = new ArmorMaterial() {
        @Override public int getDurabilityForType(ArmorItem.Type type) { return 0; } // не ломается
        @Override public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 6;
                case CHESTPLATE -> 12;
                case LEGGINGS -> 10;
                case BOOTS -> 6;
            };
        }
        @Override public int getEnchantmentValue() { return 30; }
        @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_NETHERITE; }
        @Override public Ingredient getRepairIngredient() { return Ingredient.of(ModItems.INFINITY_INGOT.get()); }
        @Override public String getName() { return Infinitum.MODID + ":infinity"; }
        @Override public float getToughness() { return 20f; }
        @Override public float getKnockbackResistance() { return 1f; }
    };

    private ModArmorMaterials() {}
}
