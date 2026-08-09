package com.therootsofancientmagic.init.worldgen;

import java.util.List;

import com.therootsofancientmagic.TheRootsOfAncientMagic;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class PlacedFeatureInit {

    public static final RegistryKey<PlacedFeature> FLOWER_DARK_PLACED_KEY =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_dark_placed")
            );

    public static final RegistryKey<PlacedFeature> FLOWER_LIGHT_PLACED_KEY =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_light_placed")
            );

    public static final RegistryKey<PlacedFeature> FLOWER_WEED_PLACED_KEY =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_weed_placed")
            );

    public static final RegistryKey<PlacedFeature> FLOWER_EARTH_PLACED_KEY =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_earth_placed")
            );


    public static final RegistryKey<PlacedFeature> FLOWER_FIRE_PLACED_KEY =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_fire_placed")
            );

    public static final RegistryKey<PlacedFeature> FLOWER_AQUA_PLACED_KEY =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_aqua_placed")
            );

    public static void bootstrap(Registerable<PlacedFeature> context) {

        RegistryEntry<ConfiguredFeature<?, ?>> configuredFeatureEntry =
                context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                        .getOrThrow(
                                ConfigureFeatureInit.FLOWER_DARK_KEY
                        );

        List<PlacementModifier> modifiers = List.of(
                RarityFilterPlacementModifier.of(16),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        context.register(
                FLOWER_DARK_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatureEntry,
                        modifiers
                )
        );

        context.register(
                FLOWER_LIGHT_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatureEntry,
                        modifiers
                )
        );

        context.register(
                FLOWER_WEED_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatureEntry,
                        modifiers
                )
        );


        context.register(
                FLOWER_EARTH_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatureEntry,
                        modifiers
                )
        );


        context.register(
                FLOWER_FIRE_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatureEntry,
                        modifiers
                )
        );

        context.register(
                FLOWER_AQUA_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatureEntry,
                        modifiers
                )
        );
    }
}