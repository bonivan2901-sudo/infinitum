package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.block.NeutronCollectorBlockEntity;
import com.ivan21.infinitum.block.NeutronCompressorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Infinitum.MODID);

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<NeutronCollectorBlockEntity>> NEUTRON_COLLECTOR =
            BLOCK_ENTITIES.register("neutron_collector", () -> BlockEntityType.Builder
                    .of(NeutronCollectorBlockEntity::new, ModBlocks.NEUTRON_COLLECTOR.get()).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<NeutronCompressorBlockEntity>> NEUTRON_COMPRESSOR =
            BLOCK_ENTITIES.register("neutron_compressor", () -> BlockEntityType.Builder
                    .of(NeutronCompressorBlockEntity::new, ModBlocks.NEUTRON_COMPRESSOR.get()).build(null));

    private ModBlockEntities() {}
}
