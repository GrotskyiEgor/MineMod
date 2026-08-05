package com.therootsofancientmagic;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.therootsofancientmagic.block.ModBlock;
import com.therootsofancientmagic.block.ModFlowerBlock;
import com.therootsofancientmagic.init.worldgen.PlacedFeatureInit;
import com.therootsofancientmagic.item.ModItem;

public class TheRootsOfAncientMagic implements ModInitializer {
	public static final String MOD_ID = "the-roots-of-ancient-magic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItem.registerModItems();

		ModBlock.registerModBlocks();

		ModFlowerBlock.registerModBlocks();
		ModFlowerBlock.RenderFlowers();

		BiomeModifications.addFeature(
			BiomeSelectors.foundInOverworld(),
			GenerationStep.Feature.VEGETAL_DECORATION,
			PlacedFeatureInit.FLOWER_DARK_PLACED_KEY
		);
	}
	
	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
