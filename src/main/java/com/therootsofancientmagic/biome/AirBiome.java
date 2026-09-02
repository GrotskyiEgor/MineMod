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
                        .waterColor(0x32D583)
                        .waterFogColor(0x168A4A)
                        .fogColor(0xB8FFB8)
                        .skyColor(0x87E887)
                        .grassColor(0x20E820)
                        .foliageColor(0x0FAF32)
                        .build()
        )
        .spawnSettings(spawnSettings.build())
        .generationSettings(generationSettings.build())
        .build();
    }
}