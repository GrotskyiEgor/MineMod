package com.therootsofancientmagic.client.biomes.fire;

import com.therootsofancientmagic.biome.ModBiomes;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class FireBiomeParticles {

    private static final Random RANDOM = Random.create();

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) {
                return;
            }

            ClientWorld world = client.world;
            BlockPos pos = client.player.getBlockPos();

            // Работаем только в Fire биоме
            if (!world.getBiome(pos)
                    .getKey()
                    .orElse(null)
                    .equals(ModBiomes.FIRE)) {
                return;
            }

            // Дым
            if (RANDOM.nextFloat() < 0.50F) {
                spawnSmoke(world, pos);
            }
        });
    }

    private static void spawnSmoke(
            ClientWorld world,
            BlockPos pos
    ) {

        for (int i = 0; i < 3; i++) {

            double x = pos.getX()
                    + 0.5
                    + (RANDOM.nextDouble() - 0.5) * 8.0;

            double y = pos.getY()
                    + 0.2
                    + RANDOM.nextDouble() * 2.5;

            double z = pos.getZ()
                    + 0.5
                    + (RANDOM.nextDouble() - 0.5) * 8.0;

            world.addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x,
                    y,
                    z,
                    (RANDOM.nextDouble() - 0.5) * 0.01,
                    0.015 + RANDOM.nextDouble() * 0.02,
                    (RANDOM.nextDouble() - 0.5) * 0.01
            );
        }
    }
}