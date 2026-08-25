package com.therootsofancientmagic.client.biomes.earth;

import com.therootsofancientmagic.biome.ModBiomes;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class EarthBiomeParticles {

    private static final Random RANDOM = Random.create();

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) {
                return;
            }

            ClientWorld world = client.world;
            BlockPos pos = client.player.getBlockPos();

            if (!world.getBiome(pos)
                    .getKey()
                    .orElse(null)
                    .equals(ModBiomes.EARTH)) {
                return;
            }

            // Серо-зелёная пыль
            if (RANDOM.nextFloat() < 0.55F) {
                spawnDust(world, pos);
            }

            // Пепел
            if (RANDOM.nextFloat() < 0.40F) {
                spawnAsh(world, pos);
            }

            // Светящиеся частицы
            if (RANDOM.nextFloat() < 0.06F) {
                spawnGlow(world, pos);
            }

            // Споры
            if (RANDOM.nextFloat() < 0.15F) {
                spawnSpore(world, pos);
            }

            // Дополнительная пыль
            if (RANDOM.nextFloat() < 0.25F) {
                spawnDust(world, pos);
            }
        });
    }

    private static double randomX(BlockPos pos, double radius) {
        return pos.getX()
                + 0.5
                + (RANDOM.nextDouble() - 0.5) * radius;
    }

    private static double randomY(BlockPos pos) {
        return pos.getY()
                + 0.5
                + RANDOM.nextDouble() * 6.0;
    }

    private static double randomZ(BlockPos pos, double radius) {
        return pos.getZ()
                + 0.5
                + (RANDOM.nextDouble() - 0.5) * radius;
    }

    private static void spawnDust(
            ClientWorld world,
            BlockPos pos
    ) {

        double x = randomX(pos, 18.0);
        double y = randomY(pos);
        double z = randomZ(pos, 18.0);

        DustParticleEffect particle =
                new DustParticleEffect(
                        new org.joml.Vector3f(
                                0.32F,
                                0.34F,
                                0.30F
                        ),
                        0.40F
                );

        world.addParticle(
                particle,
                x,
                y,
                z,
                (RANDOM.nextDouble() - 0.5) * 0.012,
                0.003 + RANDOM.nextDouble() * 0.008,
                (RANDOM.nextDouble() - 0.5) * 0.012
        );
    }

    private static void spawnAsh(
            ClientWorld world,
            BlockPos pos
    ) {

        double x = randomX(pos, 20.0);
        double y = randomY(pos);
        double z = randomZ(pos, 20.0);

        world.addParticle(
                ParticleTypes.ASH,
                x,
                y,
                z,
                (RANDOM.nextDouble() - 0.5) * 0.012,
                -0.003 - RANDOM.nextDouble() * 0.008,
                (RANDOM.nextDouble() - 0.5) * 0.012
        );
    }

    private static void spawnGlow(
            ClientWorld world,
            BlockPos pos
    ) {

        double x = randomX(pos, 16.0);
        double y = randomY(pos);
        double z = randomZ(pos, 16.0);

        world.addParticle(
                ParticleTypes.END_ROD,
                x,
                y,
                z,
                (RANDOM.nextDouble() - 0.5) * 0.003,
                0.002,
                (RANDOM.nextDouble() - 0.5) * 0.003
        );
    }

    private static void spawnSpore(
            ClientWorld world,
            BlockPos pos
    ) {

        double x = randomX(pos, 18.0);
        double y = randomY(pos);
        double z = randomZ(pos, 18.0);

        world.addParticle(
                ParticleTypes.SPORE_BLOSSOM_AIR,
                x,
                y,
                z,
                (RANDOM.nextDouble() - 0.5) * 0.004,
                0.001 + RANDOM.nextDouble() * 0.003,
                (RANDOM.nextDouble() - 0.5) * 0.004
        );
    }
}