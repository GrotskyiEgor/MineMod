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


            int xStart = width - 24; 
            int yStart = height - 25; 

            for (int i = 0; i < 10; i++) {
                int threshold = i * 10;


                int yPos = yStart - (i * 18);


                if (currentMana > threshold) {
                    // Рисует заполнений орб
                    drawContext.drawTexture(MANA_TEXTURE, xStart, yPos, 0, 0, 16, 16, 32, 16);
                } else {
                    // Рисует пустой орб
                    drawContext.drawTexture(MANA_TEXTURE, xStart, yPos, 16, 0, 16, 16, 32, 16);
                }
            }
        }
    }
}
