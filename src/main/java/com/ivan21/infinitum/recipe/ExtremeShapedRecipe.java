package com.ivan21.infinitum.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ivan21.infinitum.registry.ModBlocks;
import com.ivan21.infinitum.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/** Форменный рецепт экстремального верстака (до 9x9). Пробел в шаблоне = пустая клетка. */
public class ExtremeShapedRecipe implements Recipe<CraftingContainer> {
    private final ResourceLocation id;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public ExtremeShapedRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        for (int ox = 0; ox <= inv.getWidth() - width; ox++) {
            for (int oy = 0; oy <= inv.getHeight() - height; oy++) {
                if (matchesAt(inv, ox, oy)) return true;
            }
        }
        return false;
    }

    private boolean matchesAt(CraftingContainer inv, int ox, int oy) {
        for (int x = 0; x < inv.getWidth(); x++) {
            for (int y = 0; y < inv.getHeight(); y++) {
                int rx = x - ox;
                int ry = y - oy;
                Ingredient ing = Ingredient.EMPTY;
                if (rx >= 0 && ry >= 0 && rx < width && ry < height) {
                    ing = ingredients.get(rx + ry * width);
                }
                if (!ing.test(inv.getItem(x + y * inv.getWidth()))) return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w >= width && h >= height;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true; // не показываем в книге рецептов ванильного верстака
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.EXTREME_CRAFTING_TABLE.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EXTREME_SHAPED.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EXTREME_TYPE.get();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public static class Serializer implements RecipeSerializer<ExtremeShapedRecipe> {
        @Override
        public ExtremeShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray patternJson = GsonHelper.getAsJsonArray(json, "pattern");
            if (patternJson.isEmpty() || patternJson.size() > 9) {
                throw new JsonParseException("Extreme recipe pattern must have 1..9 rows: " + id);
            }
            String[] pattern = new String[patternJson.size()];
            int w = 0;
            for (int i = 0; i < pattern.length; i++) {
                pattern[i] = GsonHelper.convertToString(patternJson.get(i), "pattern[" + i + "]");
                w = Math.max(w, pattern[i].length());
            }
            if (w > 9) throw new JsonParseException("Extreme recipe pattern too wide: " + id);
            int h = pattern.length;

            Map<Character, Ingredient> key = new HashMap<>();
            key.put(' ', Ingredient.EMPTY);
            JsonObject keyJson = GsonHelper.getAsJsonObject(json, "key");
            for (Map.Entry<String, JsonElement> e : keyJson.entrySet()) {
                if (e.getKey().length() != 1) throw new JsonParseException("Invalid key '" + e.getKey() + "' in " + id);
                key.put(e.getKey().charAt(0), Ingredient.fromJson(e.getValue()));
            }

            NonNullList<Ingredient> ingredients = NonNullList.withSize(w * h, Ingredient.EMPTY);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < pattern[y].length(); x++) {
                    char c = pattern[y].charAt(x);
                    Ingredient ing = key.get(c);
                    if (ing == null) throw new JsonParseException("Symbol '" + c + "' not defined in key: " + id);
                    ingredients.set(x + y * w, ing);
                }
            }
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new ExtremeShapedRecipe(id, w, h, ingredients, result);
        }

        @Override
        public @Nullable ExtremeShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int w = buf.readVarInt();
            int h = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(w * h, Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack result = buf.readItem();
            return new ExtremeShapedRecipe(id, w, h, ingredients, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ExtremeShapedRecipe recipe) {
            buf.writeVarInt(recipe.width);
            buf.writeVarInt(recipe.height);
            for (Ingredient ing : recipe.ingredients) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.result);
        }
    }
}
