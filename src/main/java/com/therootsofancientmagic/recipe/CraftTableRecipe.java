package com.therootsofancientmagic.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CraftTableRecipe implements CraftingRecipe {

    private final ShapedRecipe wrapped;

    public CraftTableRecipe(ShapedRecipe wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        return this.wrapped.matches(inventory, world);
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        return this.wrapped.craft(inventory, registryManager);
    }

    @Override
    public boolean fits(int width, int height) {
        return this.wrapped.fits(width, height);
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.wrapped.getOutput(registryManager);
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.wrapped.getIngredients();
    }

    @Override
    public String getGroup() {
        return this.wrapped.getGroup();
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return this.wrapped.getCategory();
    }

    @Override
    public Identifier getId() {
        return this.wrapped.getId();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRAFT_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CUSTOM_CRAFTING_TYPE;
    }

    public static class Serializer implements RecipeSerializer<CraftTableRecipe> {

        @Override
        public CraftTableRecipe read(Identifier id, JsonObject json) {
            ShapedRecipe shaped = RecipeSerializer.SHAPED.read(id, json);
            return new CraftTableRecipe(shaped);
        }

        @Override
        public CraftTableRecipe read(Identifier id, PacketByteBuf buf) {
            ShapedRecipe shaped = RecipeSerializer.SHAPED.read(id, buf);
            return new CraftTableRecipe(shaped);
        }

        @Override
        public void write(PacketByteBuf buf, CraftTableRecipe recipe) {
            RecipeSerializer.SHAPED.write(buf, recipe.wrapped);
        }
    }
}
