package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.item.DuplicationCrystalItem;
import com.ivan21.infinitum.item.EndestPearlItem;
import com.ivan21.infinitum.item.InfinityAxeItem;
import com.ivan21.infinitum.item.InfinityBowItem;
import com.ivan21.infinitum.item.InfinityClockItem;
import com.ivan21.infinitum.item.InfinityHoeItem;
import com.ivan21.infinitum.item.InfinityShovelItem;
import com.ivan21.infinitum.item.InfinityTotemItem;
import com.ivan21.infinitum.item.InfinityUmbrellaItem;
import com.ivan21.infinitum.item.SkullfireSwordItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Tiers;
import com.ivan21.infinitum.item.GlowingItem;
import com.ivan21.infinitum.item.InfinityArmorItem;
import com.ivan21.infinitum.item.InfinityPickaxeItem;
import com.ivan21.infinitum.item.InfinitySwordItem;
import com.ivan21.infinitum.item.ModArmorMaterials;
import com.ivan21.infinitum.item.ModTiers;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Infinitum.MODID);

    // ---------- Материалы ----------
    public static final RegistryObject<Item> DIAMOND_LATTICE = simple("diamond_lattice", Rarity.UNCOMMON);
    public static final RegistryObject<Item> CRYSTAL_MATRIX_INGOT = glowing("crystal_matrix_ingot", Rarity.RARE);
    public static final RegistryObject<Item> NEUTRON_PILE = simple("neutron_pile", Rarity.UNCOMMON);
    public static final RegistryObject<Item> NEUTRON_NUGGET = simple("neutron_nugget", Rarity.UNCOMMON);
    public static final RegistryObject<Item> NEUTRONIUM_INGOT = simple("neutronium_ingot", Rarity.RARE);

    // ---------- Сингулярности ----------
    public static final RegistryObject<Item> IRON_SINGULARITY = glowing("iron_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> GOLD_SINGULARITY = glowing("gold_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> COPPER_SINGULARITY = glowing("copper_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> REDSTONE_SINGULARITY = glowing("redstone_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> LAPIS_SINGULARITY = glowing("lapis_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> DIAMOND_SINGULARITY = glowing("diamond_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> EMERALD_SINGULARITY = glowing("emerald_singularity", Rarity.EPIC);
    public static final RegistryObject<Item> NETHERITE_SINGULARITY = glowing("netherite_singularity", Rarity.EPIC);

    // ---------- Бесконечность ----------
    public static final RegistryObject<Item> INFINITY_CATALYST = glowing("infinity_catalyst", Rarity.EPIC);
    public static final RegistryObject<Item> INFINITY_INGOT = glowing("infinity_ingot", Rarity.EPIC);

    // ---------- Поблажка: кристалл дюпа ----------
    public static final RegistryObject<Item> DUPLICATION_CRYSTAL = ITEMS.register("duplication_crystal",
            () -> new DuplicationCrystalItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    // ---------- Инструменты ----------
    public static final RegistryObject<Item> INFINITY_SWORD = ITEMS.register("infinity_sword",
            () -> new InfinitySwordItem(ModTiers.INFINITY, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> INFINITY_PICKAXE = ITEMS.register("infinity_pickaxe",
            () -> new InfinityPickaxeItem(ModTiers.INFINITY, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> INFINITY_AXE = ITEMS.register("infinity_axe",
            () -> new InfinityAxeItem(ModTiers.INFINITY, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> INFINITY_SHOVEL = ITEMS.register("infinity_shovel",
            () -> new InfinityShovelItem(ModTiers.INFINITY, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> INFINITY_HOE = ITEMS.register("infinity_hoe",
            () -> new InfinityHoeItem(ModTiers.INFINITY, new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> INFINITY_BOW = ITEMS.register("infinity_bow",
            () -> new InfinityBowItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> SKULLFIRE_SWORD = ITEMS.register("skullfire_sword",
            () -> new SkullfireSwordItem(Tiers.NETHERITE, new Item.Properties().rarity(Rarity.RARE).fireResistant()));

    // ---------- Полезности ----------
    public static final RegistryObject<Item> INFINITY_CLOCK = ITEMS.register("infinity_clock",
            () -> new InfinityClockItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> INFINITY_UMBRELLA = ITEMS.register("infinity_umbrella",
            () -> new InfinityUmbrellaItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> INFINITY_TOTEM = ITEMS.register("infinity_totem",
            () -> new InfinityTotemItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> ENDEST_PEARL = ITEMS.register("endest_pearl",
            () -> new EndestPearlItem(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));

    // ---------- Еда ----------
    public static final RegistryObject<Item> ULTIMATE_STEW = ITEMS.register("ultimate_stew",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).food(new FoodProperties.Builder()
                    .nutrition(20).saturationMod(20f).alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 300, 1), 1f)
                    .build())));
    public static final RegistryObject<Item> COSMIC_MEATBALLS = ITEMS.register("cosmic_meatballs",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC).food(new FoodProperties.Builder()
                    .nutrition(20).saturationMod(20f).meat().alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 300, 1), 1f)
                    .build())));

    // ---------- Броня ----------
    public static final RegistryObject<Item> INFINITY_HELMET = armor("infinity_helmet", ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> INFINITY_CHESTPLATE = armor("infinity_chestplate", ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> INFINITY_LEGGINGS = armor("infinity_leggings", ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> INFINITY_BOOTS = armor("infinity_boots", ArmorItem.Type.BOOTS);

    private ModItems() {}

    private static RegistryObject<Item> simple(String name, Rarity rarity) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().rarity(rarity)));
    }

    private static RegistryObject<Item> glowing(String name, Rarity rarity) {
        return ITEMS.register(name, () -> new GlowingItem(new Item.Properties().rarity(rarity).fireResistant()));
    }

    private static RegistryObject<Item> armor(String name, ArmorItem.Type type) {
        return ITEMS.register(name, () -> new InfinityArmorItem(ModArmorMaterials.INFINITY, type,
                new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    }
}
