package com.therootsofancientmagic.client;

import com.therootsofancientmagic.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ManaHudOverlay implements HudRenderCallback {
    private static final Identifier MANA_TEXTURE = new Identifier("the-roots-of-ancient-magic", "textures/hud/mana.png");


    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if (!client.player.isCreative() && !client.player.isSpectator()) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            int currentMana = 0;
            if (client.player instanceof IEntityDataSaver dataSaver) {
                NbtCompound nbt = dataSaver.getPersistentData();
                currentMana = nbt.getInt("mana");
            }

            // Рассчитывает стартовую позицию над полоской голода
            int xStart = width / 2 + 10; 
            int yStart = height - 49;

            // Перебирает ровно 10 кружочков на экране
            for (int i = 0; i < 10; i++) {
                // Каждый кружочек отвечает за 10 единиц мани
                int threshold = i * 10;

                int xPos = xStart + (i * 8);

                if (currentMana > threshold) {
                    // если есть мана то кружок стает фиолетовим
                    drawContext.drawTexture(MANA_TEXTURE, xPos, yStart, 0, 0, 9, 9, 18, 9);
                } else {
                    drawContext.drawTexture(MANA_TEXTURE, xPos, yStart, 9, 0, 9, 9, 18, 9);
                }
            }
        }
    }
}
