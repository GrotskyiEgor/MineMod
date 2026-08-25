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
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.carver.ConfiguredCarver;

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

        // Обычные пещеры и руды
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);

        // Пустынная растительность
        // DefaultBiomeFeatures.addDesertGrass(generationSettings);

        // Твои собственные фичи
        addFeature(
                generationSettings,
                featureLookup,
                "flower_fire_placed",
                GenerationStep.Feature.VEGETAL_DECORATION
        );

        addFeature(
                generationSettings,
                featureLookup,
                "fire_lava_lake",
                GenerationStep.Feature.LAKES
        );

        SpawnSettings.Builder spawnSettings =
                new SpawnSettings.Builder();

        // Пустынные мобы
        DefaultBiomeFeatures.addDesertMobs(spawnSettings);

        return new Biome.Builder()
                .precipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(0xD84315)
                                .waterFogColor(0x7F1D0C)
                                .fogColor(0xD8896B)
                                .skyColor(0xC96B45)
                                .grassColor(0xA63D2F)
                                .foliageColor(0x8B321F)
                                .build()
                )
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }

    private static void addFeature(
            GenerationSettings.LookupBackedBuilder gen,
            RegistryEntryLookup<PlacedFeature> featureLookup,
            String id,
            GenerationStep.Feature step
    ) {
        gen.feature(
                step,
                featureLookup.getOrThrow(
                        RegistryKey.of(
                                RegistryKeys.PLACED_FEATURE,
                                TheRootsOfAncientMagic.id(id)
                        )
                )
        );
    }
}