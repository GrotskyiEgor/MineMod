package com.therootsofancientmagic.datagen;

import com.therootsofancientmagic.world.ModBiomGenerator;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;

public class ModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

        FabricDataGenerator.Pack pack =
                fabricDataGenerator.createPack();

        pack.addProvider(
                (output, registriesFuture) ->
                        new com.therootsofancientmagic.world.ModWordGenerator(
                                output,
                                registriesFuture
                        )
        );
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        ModBiomGenerator.bootstrapRegistries(registryBuilder);
    }
}