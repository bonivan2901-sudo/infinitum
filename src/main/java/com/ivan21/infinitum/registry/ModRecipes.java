package com.ivan21.infinitum.registry;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.recipe.DuplicationRecipe;
import com.ivan21.infinitum.recipe.ExtremeShapedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Infinitum.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Infinitum.MODID);

    public static final RegistryObject<RecipeType<ExtremeShapedRecipe>> EXTREME_TYPE = TYPES.register("extreme_crafting",
            () -> RecipeType.simple(new ResourceLocation(Infinitum.MODID, "extreme_crafting")));

    public static final RegistryObject<RecipeSerializer<ExtremeShapedRecipe>> EXTREME_SHAPED =
            SERIALIZERS.register("extreme_shaped", ExtremeShapedRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<DuplicationRecipe>> DUPLICATION =
            SERIALIZERS.register("duplication", () -> new SimpleCraftingRecipeSerializer<>(DuplicationRecipe::new));

    private ModRecipes() {}
}
