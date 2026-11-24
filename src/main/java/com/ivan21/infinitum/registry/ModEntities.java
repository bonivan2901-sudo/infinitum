package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.entity.EndestPearlEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Infinitum.MODID);

    public static final RegistryObject<EntityType<EndestPearlEntity>> ENDEST_PEARL = ENTITIES.register("endest_pearl",
            () -> EntityType.Builder.<EndestPearlEntity>of(EndestPearlEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("endest_pearl"));

    private ModEntities() {}
}
