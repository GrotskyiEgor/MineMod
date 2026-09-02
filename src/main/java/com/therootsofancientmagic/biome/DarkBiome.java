package com.therootsofancientmagic.biome;

import com.therootsofancientmagic.TheRootsOfAncientMagic;

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

public class DarkBiome {

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
        DefaultBiomeFeatures.addDungeons(generationSettings);
        DefaultBiomeFeatures.addTaigaGrass(generationSettings);

        generationSettings.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                featureLookup.getOrThrow(
                        RegistryKey.of(
                                RegistryKeys.PLACED_FEATURE,
                                TheRootsOfAncientMagic.id("dark_spruce")
                        )
                )
        );

        generationSettings.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                featureLookup.getOrThrow(
                        RegistryKey.of(
                                RegistryKeys.PLACED_FEATURE,
                                TheRootsOfAncientMagic.id("dark_red_mushroom")
                        )
                )
        );

        DefaultBiomeFeatures.addSprings(generationSettings);

        SpawnSettings.Builder spawnSettings =
                new SpawnSettings.Builder();

        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.15F)
                .downfall(0.9F)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(0x6A3FA0)
                                .waterFogColor(0x3A1F5C)
                                .fogColor(0x5A3A7A)
                                .skyColor(0x7B4FB3)
                                .grassColor(0xA855F7)
                                .foliageColor(0x9333EA)
                                .build()
                )
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
}