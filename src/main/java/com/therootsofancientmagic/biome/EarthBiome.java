package com.therootsofancientmagic.biome;

import com.therootsofancientmagic.TheRootsOfAncientMagic;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class EarthBiome {

    public static Biome create(
            RegistryEntryLookup<PlacedFeature> featureLookup,
            RegistryEntryLookup<ConfiguredCarver<?>> carverLookup
    ) {

        GenerationSettings.LookupBackedBuilder generationSettings =
                new GenerationSettings.LookupBackedBuilder(
                        featureLookup,
                        carverLookup
                );

        // Пещеры
        DefaultBiomeFeatures.addLandCarvers(generationSettings);

        // Геоды
        DefaultBiomeFeatures.addAmethystGeodes(generationSettings);

        // Руды
        DefaultBiomeFeatures.addDefaultOres(generationSettings);

        // Подземелья
        DefaultBiomeFeatures.addDungeons(generationSettings);

        // 🌿 ТРАВА
        addVanillaFeature(
                generationSettings,
                featureLookup,
                "patch_grass_plain"
        );

        // 🌳 ТВОЁ ДЕРЕВО
        addModFeature(
                generationSettings,
                featureLookup,
                "earth_dark_oak"
        );

        // 🍄 ТВОИ ГРИБЫ
        addModFeature(
                generationSettings,
                featureLookup,
                "earth_brown_mushroom"
        );

        addModFeature(
                generationSettings,
                featureLookup,
                "earth_huge_brown_mushroom"
        );

        // Источники воды/лавы
        DefaultBiomeFeatures.addSprings(generationSettings);

        SpawnSettings.Builder spawnSettings =
                new SpawnSettings.Builder();

        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.5F)
                .downfall(1.0F)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(0x3F7180)
                                .waterFogColor(0x304F58)
                                .fogColor(0x777A78)
                                .skyColor(0x777A7D)
                                .grassColor(0x77786E)
                                .foliageColor(0x5F6259)
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
                                Identifier.of(
                                        "minecraft",
                                        featureId
                                )
                        )
                )
        );
    }

    private static void addModFeature(
            GenerationSettings.LookupBackedBuilder gen,
            RegistryEntryLookup<PlacedFeature> featureLookup,
            String featureId
    ) {
        gen.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                featureLookup.getOrThrow(
                        RegistryKey.of(
                                RegistryKeys.PLACED_FEATURE,
                                TheRootsOfAncientMagic.id(featureId)
                        )
                )
        );
    }
}