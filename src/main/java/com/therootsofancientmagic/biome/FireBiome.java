package com.therootsofancientmagic.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class FireBiome {

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

        addVanillaFeature(
                generationSettings,
                featureLookup,
                "trees_savanna"
        );

        DefaultBiomeFeatures.addFossils(generationSettings);
        DefaultBiomeFeatures.addDesertVegetation(generationSettings);

        DefaultBiomeFeatures.addSprings(generationSettings);

        SpawnSettings.Builder spawnSettings =
                new SpawnSettings.Builder();

        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        return new Biome.Builder()
                .precipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(0xE64A19)
                                .waterFogColor(0x8D2B0B)
                                .fogColor(0xD8A48F)
                                .skyColor(0xC97B55)
                                .grassColor(0xB0413E)
                                .foliageColor(0x8B4513)
                                .build()
                )
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }

    private static void addVanillaFeature(
            GenerationSettings.LookupBackedBuilder gen,
            RegistryEntryLookup<PlacedFeature> featureLookup,
            String featureId
    ) {
        gen.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                featureLookup.getOrThrow(
                        RegistryKey.of(
                                RegistryKeys.PLACED_FEATURE,
                                net.minecraft.util.Identifier.of(
                                        "minecraft",
                                        featureId
                                )
                        )
                )
        );
    }
}