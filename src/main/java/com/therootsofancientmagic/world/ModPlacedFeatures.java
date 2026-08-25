package com.therootsofancientmagic.world;

import com.therootsofancientmagic.TheRootsOfAncientMagic;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> EARTH_DARK_OAK =
            RegistryKey.of(
                    RegistryKeys.PLACED_FEATURE,
                    TheRootsOfAncientMagic.id("earth_dark_oak")
            );

    // public static void bootstrap(
    //         Registerable<PlacedFeature> context
    // ) {

    //     context.register(
    //             EARTH_DARK_OAK,
    //             new PlacedFeature(
    //                     context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)
    //                             .getOrThrow(
    //                                     net.minecraft.world.gen.feature.ConfiguredFeatures.DARK_OAK
    //                             ),
    //                     java.util.List.of()
    //             )
    //     );
    // }
}
