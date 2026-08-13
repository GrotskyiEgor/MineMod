package com.therootsofancientmagic.biome;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class ModBiomes {

    public static final RegistryKey<Biome> ANCIENT_FOREST = RegistryKey.of(
            RegistryKeys.BIOME,
            new Identifier(
                    TheRootsOfAncientMagic.MOD_ID,
                    "ancient_forest"
            )
    );

    public static void bootstrap(Registerable<Biome> context) {
        context.register(
                ANCIENT_FOREST,
                createAncientForest()
        );
    }

    private static Biome createAncientForest() {

        SpawnSettings spawnSettings = new SpawnSettings.Builder()
                .build();

        GenerationSettings generationSettings =
                new GenerationSettings.Builder()
                        .build();

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects(
                        new BiomeEffects.Builder()
                                .waterColor(4159204)
                                .waterFogColor(329011)
                                .fogColor(12638463)
                                .skyColor(7907327)
                                .grassColor(8490267)
                                .foliageColor(6796280)
                                .build()
                )
                .spawnSettings(spawnSettings)
                .generationSettings(generationSettings)
                .build();
    }
}