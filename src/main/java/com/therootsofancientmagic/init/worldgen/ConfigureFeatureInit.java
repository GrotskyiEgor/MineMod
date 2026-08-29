package com.therootsofancientmagic.init.worldgen;

import java.util.List;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.block.ModFlowerBlock;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
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

    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWER_AIR_KEY =
        RegistryKey.of(
                RegistryKeys.CONFIGURED_FEATURE,
                TheRootsOfAncientMagic.id("flower_air_patch")
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
    

    
    public static final RegistryKey<ConfiguredFeature<?, ?>> EARTH_DARK_OAK_KEY =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    TheRootsOfAncientMagic.id("earth_dark_oak")
            );
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> EARTH_BROWN_MUSHROOM_KEY =
        RegistryKey.of(
                RegistryKeys.CONFIGURED_FEATURE,
                TheRootsOfAncientMagic.id("earth_brown_mushroom")
        );
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> EARTH_HUGE_BROWN_MUSHROOM_KEY =
        RegistryKey.of(
                RegistryKeys.CONFIGURED_FEATURE,
                TheRootsOfAncientMagic.id("earth_huge_brown_mushroom")
        );

    public static final List<RegistryKey<ConfiguredFeature<?, ?>>> FLOWERS = List.of(FLOWER_DARK_KEY, FLOWER_LIGHT_KEY, FLOWER_AIR_KEY, FLOWER_EARTH_KEY, FLOWER_FIRE_KEY, FLOWER_AQUA_KEY);

    private static Block getBlockForKey(RegistryKey<ConfiguredFeature<?, ?>> key) {
        String path = key.getValue().getPath();

        return switch(path) {
                case "flower_dark_patch" -> ModFlowerBlock.FLOWER_DARK;
                case "flower_light_patch" -> ModFlowerBlock.FLOWER_LIGHT;
                case "flower_air_patch" -> ModFlowerBlock.FLOWER_AIR;
                case "flower_earth_patch" -> ModFlowerBlock.FLOWER_EARTH;
                case "flower_fire_patch" -> ModFlowerBlock.FLOWER_FIRE;
                case "flower_aqua_patch" -> ModFlowerBlock.FLOWER_AQUA;
                default -> throw new IllegalArgumentException("Unknown flower patch");
        };
    }
    
    private static SimpleBlockFeatureConfig getFlowerConfig(Block blockFlower) {
        return new SimpleBlockFeatureConfig(BlockStateProvider.of(blockFlower.getDefaultState()));
    }

    private static RandomPatchFeatureConfig getPatchConfig(Registerable<ConfiguredFeature<?, ?>> context, SimpleBlockFeatureConfig flowerConfig) {
        return new RandomPatchFeatureConfig(
                        32,
                        6,
                        2,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                flowerConfig
                        )
                );
    }
    
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        for (RegistryKey<ConfiguredFeature<?, ?>> key : FLOWERS) {
            Block blockFlower = getBlockForKey(key);
            SimpleBlockFeatureConfig flower = getFlowerConfig(blockFlower);
    
            context.register(
                    key,
                    new ConfiguredFeature<>(
                        Feature.RANDOM_PATCH,
                        getPatchConfig(context, flower)
                )
            );
        };

        context.register(
                EARTH_BROWN_MUSHROOM_KEY,
                new ConfiguredFeature<>(
                        Feature.RANDOM_PATCH,
                        new RandomPatchFeatureConfig(
                                64,
                                7,
                                3,
                                PlacedFeatures.createEntry(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockFeatureConfig(
                                                BlockStateProvider.of(
                                                        Blocks.BROWN_MUSHROOM.getDefaultState()
                                                )
                                        )
                                )
                        )
                )
        );

        context.register(
                EARTH_HUGE_BROWN_MUSHROOM_KEY,
                new ConfiguredFeature<>(
                        Feature.HUGE_BROWN_MUSHROOM,
                        new HugeMushroomFeatureConfig(
                                BlockStateProvider.of(
                                        Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()
                                ),
                                BlockStateProvider.of(
                                        Blocks.MUSHROOM_STEM.getDefaultState()
                                ),
                                4
                        )
                )
        );
    }
}