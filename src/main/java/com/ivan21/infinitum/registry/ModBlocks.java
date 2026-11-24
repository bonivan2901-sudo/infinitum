package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.block.ExtremeCraftingTableBlock;
import com.ivan21.infinitum.block.NeutronCollectorBlock;
import com.ivan21.infinitum.block.NeutronCompressorBlock;
import com.ivan21.infinitum.block.SparkleBlock;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Infinitum.MODID);

    // ---------- Реалистичные природные камни (генерируются в мире) ----------
    public static final RegistryObject<Block> MARBLE = stone("marble", MapColor.QUARTZ, 1.5f);
    public static final RegistryObject<Block> POLISHED_MARBLE = stone("polished_marble", MapColor.QUARTZ, 1.5f);
    public static final RegistryObject<Block> MARBLE_BRICKS = stone("marble_bricks", MapColor.QUARTZ, 1.5f);
    public static final RegistryObject<Block> LIMESTONE = stone("limestone", MapColor.SAND, 1.2f);
    public static final RegistryObject<Block> POLISHED_LIMESTONE = stone("polished_limestone", MapColor.SAND, 1.2f);
    public static final RegistryObject<Block> LIMESTONE_BRICKS = stone("limestone_bricks", MapColor.SAND, 1.2f);
    public static final RegistryObject<Block> SLATE = stone("slate", MapColor.DEEPSLATE, 2.0f);
    public static final RegistryObject<Block> SLATE_TILES = stone("slate_tiles", MapColor.DEEPSLATE, 2.0f);
    public static final RegistryObject<Block> TRAVERTINE = stone("travertine", MapColor.TERRACOTTA_WHITE, 1.4f);
    public static final RegistryObject<Block> POLISHED_TRAVERTINE = stone("polished_travertine", MapColor.TERRACOTTA_WHITE, 1.4f);
    public static final RegistryObject<Block> GNEISS = stone("gneiss", MapColor.STONE, 2.2f);
    public static final RegistryObject<Block> POLISHED_GNEISS = stone("polished_gneiss", MapColor.STONE, 2.2f);

    // ---------- Индустриальные блоки ----------
    public static final RegistryObject<Block> ASPHALT = stone("asphalt", MapColor.COLOR_BLACK, 2.0f);
    public static final RegistryObject<Block> REINFORCED_CONCRETE = register("reinforced_concrete", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(10f, 1200f)
                    .requiresCorrectToolForDrops().sound(SoundType.STONE)), Rarity.COMMON);
    public static final RegistryObject<Block> RUSTY_IRON_BLOCK = register("rusty_iron_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(5f, 6f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL)), Rarity.COMMON);

    // ---------- Сжатые блоки ----------
    public static final RegistryObject<Block> COMPRESSED_COBBLESTONE = stone("compressed_cobblestone", MapColor.STONE, 3f);
    public static final RegistryObject<Block> DOUBLE_COMPRESSED_COBBLESTONE = stone("double_compressed_cobblestone", MapColor.STONE, 5f);
    public static final RegistryObject<Block> TRIPLE_COMPRESSED_COBBLESTONE = stone("triple_compressed_cobblestone", MapColor.STONE, 8f);
    public static final RegistryObject<Block> COMPRESSED_DIRT = register("compressed_dirt", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(1.5f).sound(SoundType.GRAVEL)), Rarity.COMMON);
    public static final RegistryObject<Block> COMPRESSED_SAND = register("compressed_sand", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(1.5f).sound(SoundType.SAND)), Rarity.COMMON);
    public static final RegistryObject<Block> COMPRESSED_GRAVEL = register("compressed_gravel", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.8f).sound(SoundType.GRAVEL)), Rarity.COMMON);
    public static final RegistryObject<Block> COMPRESSED_NETHERRACK = stone("compressed_netherrack", MapColor.NETHER, 2f);

    // ---------- Блоки в стиле «Бесконечности» ----------
    public static final RegistryObject<Block> CRYSTAL_MATRIX_BLOCK = register("crystal_matrix_block", () -> new SparkleBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(20f, 1000f)
                    .requiresCorrectToolForDrops().sound(SoundType.AMETHYST).lightLevel(s -> 8), () -> ParticleTypes.END_ROD, 6), Rarity.RARE);
    public static final RegistryObject<Block> NEUTRONIUM_BLOCK = register("neutronium_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(50f, 3600000f)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)), Rarity.RARE);
    public static final RegistryObject<Block> INFINITY_BLOCK = register("infinity_block", () -> new SparkleBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(100f, 3600000f)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).lightLevel(s -> 15), () -> ParticleTypes.END_ROD, 2), Rarity.EPIC);

    // ---------- Механизмы ----------
    public static final RegistryObject<Block> EXTREME_CRAFTING_TABLE = register("extreme_crafting_table", () -> new ExtremeCraftingTableBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(8f, 2000f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL).lightLevel(s -> 6)), Rarity.EPIC);
    public static final RegistryObject<Block> NEUTRON_COLLECTOR = register("neutron_collector", () -> new NeutronCollectorBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(8f, 2000f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL).lightLevel(s -> 4)), Rarity.RARE);

    public static final RegistryObject<Block> NEUTRON_COMPRESSOR = register("neutron_compressor", () -> new NeutronCompressorBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(8f, 2000f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL).lightLevel(s -> 5)), Rarity.RARE);

    private ModBlocks() {}

    private static RegistryObject<Block> stone(String name, MapColor color, float hardness) {
        return register(name, () -> new Block(BlockBehaviour.Properties.of().mapColor(color)
                .strength(hardness, 6f).requiresCorrectToolForDrops().sound(SoundType.STONE)), Rarity.COMMON);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Rarity rarity) {
        RegistryObject<T> obj = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(obj.get(), new Item.Properties().rarity(rarity)));
        return obj;
    }
}
