package com.therootsofancientmagic.init.worldgen;

import java.util.List;

import com.therootsofancientmagic.TheRootsOfAncientMagic;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.MiscConfiguredFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.placementmodifier.EnvironmentScanPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;


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

    public static final RegistryKey<PlacedFeature> FLOWER_AIR_PLACED_KEY =
        RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                TheRootsOfAncientMagic.id("flower_air_placed")
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



    public static final RegistryKey<PlacedFeature> EARTH_DARK_OAK_KEY =
        RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                TheRootsOfAncientMagic.id("earth_dark_oak")
        );

    public static final RegistryKey<PlacedFeature> EARTH_BROWN_MUSHROOM_PLACED_KEY =
        RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                TheRootsOfAncientMagic.id("earth_brown_mushroom")
        );

    public static final RegistryKey<PlacedFeature> EARTH_HUGE_BROWN_MUSHROOM_PLACED_KEY =
        RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                TheRootsOfAncientMagic.id("earth_huge_brown_mushroom")
        );

    public static final RegistryKey<PlacedFeature> FIRE_LAVA_LAKE_KEY =
        RegistryKey.of(
            RegistryKeys.PLACED_FEATURE,
            TheRootsOfAncientMagic.id("fire_lava_lake")
        );

    public static final List<RegistryKey<PlacedFeature>> FLOWERS_KEYS = List.of(
        FLOWER_DARK_PLACED_KEY, 
        FLOWER_LIGHT_PLACED_KEY, 
        FLOWER_AIR_PLACED_KEY, 
        FLOWER_EARTH_PLACED_KEY,
        FLOWER_AQUA_PLACED_KEY
    );

    private static RegistryKey<ConfiguredFeature<?, ?>> getKeyForKeconfiguredFeatureEntry(RegistryKey<PlacedFeature> key) {
        String path = key.getValue().getPath();

        return switch(path) {
                case "flower_dark_placed" -> ConfigureFeatureInit.FLOWER_DARK_KEY;
                case "flower_light_placed" -> ConfigureFeatureInit.FLOWER_LIGHT_KEY;
                case "flower_air_placed" -> ConfigureFeatureInit.FLOWER_AIR_KEY;
                case "flower_earth_placed" -> ConfigureFeatureInit.FLOWER_EARTH_KEY;
                case "flower_fire_placed" -> ConfigureFeatureInit.FLOWER_FIRE_KEY;
                case "flower_aqua_placed" -> ConfigureFeatureInit.FLOWER_AQUA_KEY;
                default -> throw new IllegalArgumentException("Unknown flower patch");
        };
    }
    
    private static RegistryEntry<ConfiguredFeature<?, ?>> getConfiguredFeatureEntry(
            Registerable<PlacedFeature> context, 
            RegistryKey<ConfiguredFeature<?, ?>> configuredKey) {
        
        return context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                .getOrThrow(configuredKey);
    }

    private static List<PlacementModifier> getModifiers() {
        List<PlacementModifier> modifiers = List.of(
            RarityFilterPlacementModifier.of(4),
            SquarePlacementModifier.of(),
            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
            BiomePlacementModifier.of()
        ); 

        return modifiers;
    }

    public static void bootstrap(Registerable<PlacedFeature> context) {

        List<PlacementModifier> modifiers = getModifiers();

        for (RegistryKey<PlacedFeature> placeKey : FLOWERS_KEYS) {
                RegistryKey<ConfiguredFeature<?, ?>> configuredKey = getKeyForKeconfiguredFeatureEntry(placeKey);
                RegistryEntry<ConfiguredFeature<?, ?>> configuredFeatureEntry = getConfiguredFeatureEntry(context, configuredKey);
        
                context.register(
                        placeKey,
                        new PlacedFeature(
                                configuredFeatureEntry,
                                modifiers
                        )
                );
        }

        context.register(
            FLOWER_FIRE_PLACED_KEY,
            new PlacedFeature(
                getConfiguredFeatureEntry(
                    context,
                    ConfigureFeatureInit.FLOWER_FIRE_KEY
                ),
                List.of(
                    RarityFilterPlacementModifier.of(2),
                    SquarePlacementModifier.of(),
                    PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                    BiomePlacementModifier.of()
                )
            )
        );

        RegistryEntry<ConfiguredFeature<?, ?>> darkOak = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getOrThrow(TreeConfiguredFeatures.DARK_OAK);

        context.register(
            EARTH_DARK_OAK_KEY,
            new PlacedFeature(
                darkOak,
                List.of(
                    CountPlacementModifier.of(4),
                    SquarePlacementModifier.of(),
                    PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                    BlockFilterPlacementModifier.of(
                        BlockPredicate.wouldSurvive(
                            Blocks.DARK_OAK_SAPLING.getDefaultState(),
                            net.minecraft.util.math.Vec3i.ZERO
                        )
                    ),
                    BiomePlacementModifier.of()
                )
            )
        );

        RegistryEntry<ConfiguredFeature<?, ?>> brownMushroom =
            context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                    .getOrThrow(ConfigureFeatureInit.EARTH_BROWN_MUSHROOM_KEY);

        context.register(
                EARTH_BROWN_MUSHROOM_PLACED_KEY,
                new PlacedFeature(
                        brownMushroom,
                        List.of(
                            CountPlacementModifier.of(2),
                            SquarePlacementModifier.of(),
                            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                            BiomePlacementModifier.of()
                    )
                )
        );

        RegistryEntry<ConfiguredFeature<?, ?>> hugeBrownMushroom =
            context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                    .getOrThrow(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM);

        context.register(
                EARTH_HUGE_BROWN_MUSHROOM_PLACED_KEY,
                new PlacedFeature(
                        hugeBrownMushroom,
                        List.of(
                                RarityFilterPlacementModifier.of(2),
                                SquarePlacementModifier.of(),
                                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                                BiomePlacementModifier.of()
                        )
                )
        );

        RegistryEntry<ConfiguredFeature<?, ?>> lavaLake =
            context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
                    .getOrThrow(MiscConfiguredFeatures.LAKE_LAVA);

        context.register(
            FIRE_LAVA_LAKE_KEY,
            new PlacedFeature(
                lavaLake,
                List.of(
                    RarityFilterPlacementModifier.of(2),
                    SquarePlacementModifier.of(),
                    PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                    BiomePlacementModifier.of()
                )
            )
        );
    }
}