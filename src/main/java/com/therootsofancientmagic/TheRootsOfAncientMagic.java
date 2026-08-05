package com.therootsofancientmagic;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.therootsofancientmagic.block.ModBlock;
import com.therootsofancientmagic.block.ModFlowerBlock;
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
	}
	
	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
