package com.therootsofancientmagic.world;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

public class ModWordGenerator extends FabricDynamicRegistryProvider {
    public ModWordGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    
    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        
    }

    @Override
    public String getName() {
        return "World Generation";
    }
}