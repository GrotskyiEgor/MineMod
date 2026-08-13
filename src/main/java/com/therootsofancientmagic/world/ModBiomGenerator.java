package com.therootsofancientmagic.world;

import com.therootsofancientmagic.biome.ModBiomes;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ModBiomGenerator {
    public static void bootstrapRegistries(RegistryBuilder registryBuilder) {

        registryBuilder.addRegistry(
                RegistryKeys.BIOME,
                ModBiomes::bootstrap
        );
    }
}
