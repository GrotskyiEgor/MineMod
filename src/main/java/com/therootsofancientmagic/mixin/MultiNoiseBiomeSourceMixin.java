package com.therootsofancientmagic.mixin;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin {

    @Inject(
            method = "getBiome",
            at = @At("RETURN"),
            cancellable = true
    )
    private void replaceForest(
            int x,
            int y,
            int z,
            MultiNoiseUtil.MultiNoiseSampler noise,
            CallbackInfoReturnable<RegistryEntry<Biome>> cir
    ) {

        RegistryEntry<Biome> original = cir.getReturnValue();

        if (original.matchesKey(net.minecraft.world.biome.BiomeKeys.FOREST)) {
            // Здесь пока нельзя просто получить RegistryEntry
            // твоего биома без RegistryLookup.
        }
    }
}