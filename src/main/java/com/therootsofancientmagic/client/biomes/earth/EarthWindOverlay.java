package com.therootsofancientmagic.client.biomes.earth;

import com.mojang.blaze3d.systems.RenderSystem;
import com.therootsofancientmagic.biome.ModBiomes;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class EarthWindOverlay {
    private static final Random RANDOM = Random.create();
    private static final Wind[] WINDS = new Wind[32];

    public static void register() {
        for (int i = 0; i < WINDS.length; i++) {
            WINDS[i] = new Wind();
            WINDS[i].reset(true);
        }

        HudRenderCallback.EVENT.register(EarthWindOverlay::render);
    }

    private static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.world == null) return;

        ClientWorld world = client.world;
        BlockPos pos = client.player.getBlockPos();

        if (!world.getBiome(pos).getKey().orElse(null).equals(ModBiomes.EARTH)) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (Wind wind : WINDS) {
            wind.update();

            if (wind.delay > 0) continue;

            for (int i = 0; i < wind.segments; i++) {
                float progress = i / (float) wind.segments;

                float x = (wind.x + progress * wind.lengthX) * width;
                float y = (wind.y + progress * wind.lengthY) * height;

                x += (float) Math.sin(wind.wave + i * 1.8F) * 2.0F;
                y += (float) Math.sin(wind.wave * 0.7F + i * 2.4F) * 1.5F;

                int alpha = (int) (
                        wind.alpha *
                        255.0F *
                        (1.0F - progress * 0.55F)
                );

                int color = (alpha << 24) | 0x92958A;

                int segmentLength = (int) (
                        wind.segmentLength *
                        (0.45F + RANDOM.nextFloat() * 0.55F)
                );

                int thickness = Math.max(
                        1,
                        (int) wind.thickness
                );

                context.fill(
                        (int) x,
                        (int) y,
                        (int) x + segmentLength,
                        (int) y + thickness,
                        color
                );
            }
        }

        RenderSystem.disableBlend();
    }

    private static class Wind {
        float x;
        float y;
        float speed;
        float lengthX;
        float lengthY;
        float segmentLength;
        float thickness;
        float alpha;
        float wave;
        float waveSpeed;
        int segments;
        int delay;

        void reset(boolean firstSpawn) {
            x = -0.15F + RANDOM.nextFloat() * 1.3F;
            y = -0.15F + RANDOM.nextFloat() * 1.3F;

            speed = 0.003F + RANDOM.nextFloat() * 0.005F;

            lengthX = 0.08F + RANDOM.nextFloat() * 0.14F;
            lengthY = 0.04F + RANDOM.nextFloat() * 0.10F;

            segmentLength = 4.0F + RANDOM.nextFloat() * 8.0F;

            thickness = 0.5F + RANDOM.nextFloat() * 0.8F;

            alpha = 0.025F + RANDOM.nextFloat() * 0.075F;

            wave = RANDOM.nextFloat() * 6.28F;
            waveSpeed = 0.025F + RANDOM.nextFloat() * 0.07F;

            segments = 3 + RANDOM.nextInt(5);

            delay = firstSpawn
                    ? RANDOM.nextInt(40)
                    : RANDOM.nextInt(25);
        }

        void update() {
            if (delay > 0) {
                delay--;
                return;
            }

            x += speed;
            y += speed * 0.25F;

            wave += waveSpeed;

            if (x > 1.2F || y > 1.2F) {
                reset(false);
                x = -0.15F + RANDOM.nextFloat() * 1.25F;
                y = -0.15F + RANDOM.nextFloat() * 1.25F;
            }
        }
    }
}