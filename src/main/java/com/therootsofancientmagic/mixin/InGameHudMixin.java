package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.magic.necklace.NecklaceItem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Unique
    private static final Identifier ICONS = new Identifier("minecraft", "textures/gui/icons.png");

    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderExtraFoodOverlay(DrawContext context, CallbackInfo ci) {
        PlayerEntity player = net.minecraft.client.MinecraftClient.getInstance().player;

        if (player == null || player.isSpectator()) return;

        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();

            boolean hasNecklace = false;
            for (int i = 0; i < inv.size(); i++) {
                ItemStack stack = inv.getStack(i);
                if (!stack.isEmpty() && stack.getItem() instanceof NecklaceItem) {
                    hasNecklace = true;
                    break;
                }
            }

            if (hasNecklace) {
                int scaledWidth = context.getScaledWindowWidth();
                int scaledHeight = context.getScaledWindowHeight();

                int right = scaledWidth / 2 + 91;
                int top = scaledHeight - 49; 

                for (int i = 0; i < 2; i++) {
                    int x = right - (i * 8) - 9;
                    int y = top;

                    context.drawTexture(ICONS, x, y, 16, 27, 9, 9);
                    context.drawTexture(ICONS, x, y, 52, 27, 9, 9);
                }
            }
        }
    }
}