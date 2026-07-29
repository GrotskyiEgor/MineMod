package com.competition;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ompetition implements ModInitializer {
	public static final String MOD_ID = "ompetition";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		ModItem.registerModItems();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
