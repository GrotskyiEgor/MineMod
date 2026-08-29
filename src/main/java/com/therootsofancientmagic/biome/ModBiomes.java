package com.therootsofancientmagic.biome;

import com.therootsofancientmagic.TheRootsOfAncientMagic;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModBiomes {

    public static final RegistryKey<Biome> DARK = key("dark");
    public static final RegistryKey<Biome> LIGHT = key("light");
    public static final RegistryKey<Biome> AIR = key("air");
    public static final RegistryKey<Biome> EARTH = key("earth");
    public static final RegistryKey<Biome> FIRE = key("fire");
    public static final RegistryKey<Biome> AQUA = key("aqua");

    private static RegistryKey<Biome> key(String path) {
        return RegistryKey.of(
                RegistryKeys.BIOME,
                TheRootsOfAncientMagic.id(path)
        );
    }

    public static void bootstrap(Registerable<Biome> context) {

        RegistryEntryLookup<PlacedFeature> featureLookup =
                context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        RegistryEntryLookup<ConfiguredCarver<?>> carverLookup =
                context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

        context.register(
                DARK,
                DarkBiome.create(featureLookup, carverLookup)
        );

        context.register(
                LIGHT,
                LightBiome.create(featureLookup, carverLookup)
        );

        context.register(
                AIR,
                AirBiome.create(featureLookup, carverLookup)
        );

        context.register(
                EARTH,
                EarthBiome.create(featureLookup, carverLookup)
        );

        context.register(
                FIRE,
                FireBiome.create(featureLookup, carverLookup)
        );

        context.register(
                AQUA,
                AquaBiome.create(featureLookup, carverLookup)
        );
    }
}