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

//     public static final RegistryKey<PlacedFeature> EARTH_DARK_OAK_KEY =
//             RegistryKey.of(
//                 RegistryKey.PLACED_FEATURE,
//                 TheRootsOfAncientMagic.id("earth_dark_oak")
//             )

    public static final List<RegistryKey<PlacedFeature>> FLOWERS_KEYS = List.of(
        FLOWER_DARK_PLACED_KEY, 
        FLOWER_LIGHT_PLACED_KEY, 
        FLOWER_WEED_PLACED_KEY, 
        FLOWER_EARTH_PLACED_KEY, 
        FLOWER_FIRE_PLACED_KEY, 
        FLOWER_AQUA_PLACED_KEY
    );

    private static RegistryKey<ConfiguredFeature<?, ?>> getKeyForKeconfiguredFeatureEntry(RegistryKey<PlacedFeature> key) {
        String path = key.getValue().getPath();

        return switch(path) {
                case "flower_dark_placed" -> ConfigureFeatureInit.FLOWER_DARK_KEY;
                case "flower_light_placed" -> ConfigureFeatureInit.FLOWER_LIGHT_KEY;
                case "flower_weed_placed" -> ConfigureFeatureInit.FLOWER_WEED_KEY;
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
                RarityFilterPlacementModifier.of(16),
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
    }
}