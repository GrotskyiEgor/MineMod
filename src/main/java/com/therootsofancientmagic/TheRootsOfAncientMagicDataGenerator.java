package com.therootsofancientmagic;

import com.therootsofancientmagic.init.worldgen.ConfigureFeatureInit;
import com.therootsofancientmagic.init.worldgen.PlacedFeatureInit;
import com.therootsofancientmagic.world.ModWordGenerator;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;


public class TheRootsOfAncientMagicDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		// FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// pack.addProvider(ModWordGenerator::new);
	}

	// @Override
	// public void buildRegistry(RegistryBuilder registryBuilder) {
	// 	registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ConfigureFeatureInit::bootstrap);
	// 	registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, PlacedFeatureInit::bootstrap);
	// }
}
