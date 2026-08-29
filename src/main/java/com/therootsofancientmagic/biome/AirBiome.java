package com.therootsofancientmagic.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class AirBiome {

    public static Biome create(
            RegistryEntryLookup<PlacedFeature> featureLookup,
            RegistryEntryLookup<ConfiguredCarver<?>> carverLookup
    ) {

        GenerationSettings.LookupBackedBuilder generationSettings =
                new GenerationSettings.LookupBackedBuilder(
                        featureLookup,
                        carverLookup
                );

        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);

        DefaultBiomeFeatures.addSavannaTrees(generationSettings);
        DefaultBiomeFeatures.addSavannaGrass(generationSettings);

        DefaultBiomeFeatures.addSprings(generationSettings);

        SpawnSettings.Builder spawnSettings =
                new SpawnSettings.Builder();

        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.6F)
                .downfall(0.5F)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(0x4FC3F7)
                                .waterFogColor(0x0288D1)
                                .fogColor(0xE6D8AD)
                                .skyColor(0xA9B4A0)
                                .grassColor(0xD4A017)
                                .foliageColor(0xCC7722)
                                .build()
                )
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
}