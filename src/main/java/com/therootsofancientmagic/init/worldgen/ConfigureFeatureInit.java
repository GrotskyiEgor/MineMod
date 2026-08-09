package com.therootsofancientmagic.init.worldgen;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.block.ModFlowerBlock;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class ConfigureFeatureInit {

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_DARK_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_dark_patch")
            );

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_LIGHT_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_light_patch")
            );

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_WEED_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_weed_patch")
            );

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_EARTH_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_earth_patch")
            );

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_FIRE_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_fire_patch")
            );

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_AQUA_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("flower_aqua_patch")
            );
    
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        SimpleBlockFeatureConfig flowerConfig =
                new SimpleBlockFeatureConfig(
                        BlockStateProvider.of(
                                ModFlowerBlock.FLOWER_DARK.getDefaultState()
                        )
                );

        RandomPatchFeatureConfig patchConfig =
                new RandomPatchFeatureConfig(
                        64,
                        6,
                        3,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                flowerConfig
                        )
                );

        context.register(
            FLOWER_DARK_KEY,
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    patchConfig
            )
        );

        context.register(
            FLOWER_LIGHT_KEY,
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    patchConfig
            )
        );

        context.register(
            FLOWER_WEED_KEY,
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    patchConfig
            )
        );

        context.register(
            FLOWER_EARTH_KEY,
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    patchConfig
            )
        );


        context.register(
            FLOWER_FIRE_KEY,
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    patchConfig
            )
        );

        context.register(
            FLOWER_AQUA_KEY,
            new ConfiguredFeature<>(
                    Feature.FLOWER,
                    patchConfig
            )
        );
    }
}