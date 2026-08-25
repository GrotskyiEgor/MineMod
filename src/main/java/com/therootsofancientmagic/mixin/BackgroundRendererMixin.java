package com.therootsofancientmagic.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.therootsofancientmagic.biome.ModBiomes;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    private static float earthFog = 0.0F;

    @Inject(
            method = "applyFog",
            at = @At("TAIL")
    )
    private static void earthMagicFog(
            Camera camera,
            BackgroundRenderer.FogType fogType,
            float viewDistance,
            boolean thickFog,
            float tickDelta,
            CallbackInfo ci
    ) {
        Entity entity = camera.getFocusedEntity();

        if (!(entity.getWorld() instanceof ClientWorld world)) {
            return;
        }

        BlockPos pos = entity.getBlockPos();

        boolean isEarth = world.getBiome(pos)
                .getKey()
                .orElse(null)
                .equals(ModBiomes.EARTH);

        float target = isEarth ? 1.0F : 0.0F;

        earthFog += (target - earthFog) * 0.08F;

        if (earthFog < 0.001F) {
            earthFog = 0.0F;
            return;
        }

        float start = 25.0F - 11.0F * earthFog;
        float end = 90.0F - 25.0F * earthFog;

        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(end);
    }
}