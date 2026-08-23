package com.therootsofancientmagic.biome;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.minecraft.registry.Registerable;
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

import java.util.function.BiConsumer;

public class ModBiomes {

    public static final RegistryKey<Biome> DARK = key("dark");
    public static final RegistryKey<Biome> LIGHT = key("light");
    public static final RegistryKey<Biome> WEED = key("weed");
    public static final RegistryKey<Biome> EARTH = key("earth");
    public static final RegistryKey<Biome> FIRE = key("fire");
    public static final RegistryKey<Biome> AQUA = key("aqua");

    private static RegistryKey<Biome> key(String path) {
        return RegistryKey.of(
                RegistryKeys.BIOME,
                new Identifier(TheRootsOfAncientMagic.MOD_ID, path)
        );
    }

    public static void bootstrap(Registerable<Biome> context) {
        RegistryEntryLookup<PlacedFeature> featureLookup =
                context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> carverLookup =
                context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

        // DARK
        context.register(DARK, createBiome(
                featureLookup, carverLookup,
                ModBiomes::addTaigaVegetation,
                false, 0.15F, 0.9F,
                0xB39DDB,
                0x9575CD,
                0x9C7CE0,
                0x7E57C2,
                0x9370DB,
                0xE1BEE7
        ));

        // LIGHT
        context.register(LIGHT, createBiome(
                featureLookup, carverLookup,
                ModBiomes::addBirchVegetation,
                false, 1.6F, 0.0F,
                0x4FC3F7,
                0x0288D1,
                0xFFF9C4,
                0x90CAF9,
                0xFFF200,
                0xFFC107
        ));

        // WEED саванна
        context.register(WEED, createBiome(
                featureLookup, carverLookup,
                ModBiomes::addSavannaVegetation,
                true, 0.6F, 0.5F,
                0x4FC3F7,
                0x0288D1,
                0xE6D8AD,
                0xA9B4A0,
                0xD4A017,
                0xCC7722
        ));

        // EARTH тёмный дубовый лес
        context.register(EARTH, createBiome(
                featureLookup, carverLookup,
                ModBiomes::addDarkOakVegetation,
                true, 0.5F, 1.0F,
                0x4FC3F7, 0x0288D1, 0xEFEBE9, 0xD7CCC8, 0xEFEBE9, 0x6D4C41
        ));

        // FIRE пустыня
        context.register(FIRE, createBiome(
                featureLookup, carverLookup,
                ModBiomes::addDesertVegetation,
                false, 2.0F, 0.0F,
                0xE64A19, 0x8D2B0B, 0xD8A48F, 0xC97B55, 0xB0413E, 0x8B4513
        ));

        // AQUA
        context.register(AQUA, createBiome(
                featureLookup, carverLookup,
                ModBiomes::addAquaVegetation,
                true, 0.5F, 0.9F,
                0x4FC3F7,
                0x0288D1,
                0xFCE4EC,
                0x81D4FA,
                0x4FC3F7,
                0xF06292
        ));
    }

    private static void addVanillaFeature(
            GenerationSettings.LookupBackedBuilder gen,
            RegistryEntryLookup<PlacedFeature> featureLookup,
            GenerationStep.Feature step,
            String vanillaPlacedFeatureId
    ) {
        gen.feature(
                step,
                featureLookup.getOrThrow(
                        RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("minecraft", vanillaPlacedFeatureId))
                )
        );
    }

    private static void addTaigaVegetation(GenerationSettings.LookupBackedBuilder gen, RegistryEntryLookup<PlacedFeature> featureLookup) {
        addVanillaFeature(gen, featureLookup, GenerationStep.Feature.VEGETAL_DECORATION, "trees_plains");
        DefaultBiomeFeatures.addTaigaGrass(gen);
        DefaultBiomeFeatures.addDefaultMushrooms(gen);
    }

    private static void addBirchVegetation(GenerationSettings.LookupBackedBuilder gen, RegistryEntryLookup<PlacedFeature> featureLookup) {
        addVanillaFeature(gen, featureLookup, GenerationStep.Feature.VEGETAL_DECORATION, "trees_savanna");
        DefaultBiomeFeatures.addDefaultFlowers(gen);
        DefaultBiomeFeatures.addForestGrass(gen);
        DefaultBiomeFeatures.addDefaultMushrooms(gen);
    }

    private static void addSavannaVegetation(GenerationSettings.LookupBackedBuilder gen, RegistryEntryLookup<PlacedFeature> featureLookup) {
        DefaultBiomeFeatures.addSavannaTrees(gen);
        DefaultBiomeFeatures.addSavannaGrass(gen);
    }

    private static void addDarkOakVegetation(GenerationSettings.LookupBackedBuilder gen, RegistryEntryLookup<PlacedFeature> featureLookup) {
        addVanillaFeature(gen, featureLookup, GenerationStep.Feature.VEGETAL_DECORATION, "dark_forest_vegetation");
        addVanillaFeature(gen, featureLookup, GenerationStep.Feature.VEGETAL_DECORATION, "brown_mushroom_normal");
    }

    private static void addDesertVegetation(GenerationSettings.LookupBackedBuilder gen, RegistryEntryLookup<PlacedFeature> featureLookup) {
        addVanillaFeature(gen, featureLookup, GenerationStep.Feature.VEGETAL_DECORATION, "trees_savanna");
        DefaultBiomeFeatures.addFossils(gen);
        DefaultBiomeFeatures.addDesertVegetation(gen);
    }

    private static void addAquaVegetation(GenerationSettings.LookupBackedBuilder gen, RegistryEntryLookup<PlacedFeature> featureLookup) {
        addVanillaFeature(gen, featureLookup, GenerationStep.Feature.VEGETAL_DECORATION, "trees_swamp");
        DefaultBiomeFeatures.addSwampVegetation(gen);
    }

    private static Biome createBiome(
            RegistryEntryLookup<PlacedFeature> featureLookup,
            RegistryEntryLookup<ConfiguredCarver<?>> carverLookup,
            BiConsumer<GenerationSettings.LookupBackedBuilder, RegistryEntryLookup<PlacedFeature>> vegetation,
            boolean precipitation,
            float temperature,
            float downfall,
            int waterColor,
            int waterFogColor,
            int fogColor,
            int skyColor,
            int grassColor,
            int foliageColor
    ) {
        GenerationSettings.LookupBackedBuilder generationSettings =
                new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);

        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);

        vegetation.accept(generationSettings, featureLookup);

        DefaultBiomeFeatures.addSprings(generationSettings);

        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        return new Biome.Builder()
                .precipitation(precipitation)
                .temperature(temperature)
                .downfall(downfall)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(waterColor)
                                .waterFogColor(waterFogColor)
                                .fogColor(fogColor)
                                .skyColor(skyColor)
                                .grassColor(grassColor)
                                .foliageColor(foliageColor)
                                .build()
                )
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
}