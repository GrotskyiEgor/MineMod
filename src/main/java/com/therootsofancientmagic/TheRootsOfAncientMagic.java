package com.therootsofancientmagic;

import com.therootsofancientmagic.block.ModBlock;
import com.therootsofancientmagic.block.ModFlowerBlock;
import com.therootsofancientmagic.init.worldgen.PlacedFeatureInit;
import com.therootsofancientmagic.item.ModItem;
import com.therootsofancientmagic.screen.FurnacePowderScreen;
import com.therootsofancientmagic.screen.CraftTableScreen;
import com.therootsofancientmagic.screen.ModScreenHandlers;
import com.therootsofancientmagic.block.entity.ModBlockEntities;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheRootsOfAncientMagic implements ModInitializer {

    public static final String MOD_ID = "the-roots-of-ancient-magic";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ModItem.registerModItems();

        ModBlock.registerModBlocks();

        ModFlowerBlock.registerModBlocks();
        ModFlowerBlock.RenderFlowers();

        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerScreenHandlers();

        HandledScreens.register(ModScreenHandlers.FURNACE_POWDER_SCREEN_HANDLER, FurnacePowderScreen::new);
        HandledScreens.register(ModScreenHandlers.CRAFT_TABLE_SCREEN_HANDLER, CraftTableScreen::new);

	registerFlowers();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public void registerFlowers() {
        for (RegistryKey<PlacedFeature> flower_placed_key : PlacedFeatureInit.FLOWERS_KEYS) {
            BiomeModifications.addFeature(
                    BiomeSelectors.foundInOverworld(),
                    GenerationStep.Feature.VEGETAL_DECORATION,
                    flower_placed_key
            );
        }
    }
}