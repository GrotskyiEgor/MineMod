package com.therootsofancientmagic.recipe;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static final RecipeType<CraftingRecipe> CUSTOM_CRAFTING_TYPE = Registry.register(
        Registries.RECIPE_TYPE,
        new Identifier(TheRootsOfAncientMagic.MOD_ID, "craft_table"),
        new RecipeType<CraftingRecipe>() {
            @Override
            public String toString() {
                return "craft_table";
            }
        }
    );

    public static void registerRecipes() {
        TheRootsOfAncientMagic.LOGGER.info("Registering Custom Recipes for " + TheRootsOfAncientMagic.MOD_ID);
    }
}