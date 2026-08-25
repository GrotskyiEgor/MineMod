package com.therootsofancientmagic.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    @Unique
    private static final Identifier INVENTORY_TEXTURE = new Identifier("minecraft", "textures/gui/container/inventory.png");

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "isClickOutsideBounds", at = @At("HEAD"), cancellable = true)
    private void allowCustomSlotClick(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> cir) {
        if (mouseX >= left - 30 && mouseX <= left && mouseY >= top && mouseY <= top + 81) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawCustomSlotTextures(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int panelX1 = this.x - 29;
        int panelY1 = this.y + 4;
        int panelX2 = this.x - 1;
        int panelY2 = this.y + 83;

        context.fill(panelX1, panelY1, panelX2, panelY2, 0xFFFFFFFF);

        context.fill(panelX1 + 1, panelY1 + 1, panelX2 - 1, panelY2 - 1, 0xFF555555);

        context.fill(panelX1 + 1, panelY1 + 1, panelX2 - 2, panelY2 - 2, 0xFFC6C6C6);

        int startX = this.x - 24;
        int startY = this.y + 7;

        for (int i = 0; i < 4; i++) {
            context.drawTexture(INVENTORY_TEXTURE, startX, startY + (i * 18), 7, 7, 18, 18);
        }

        int lastSlotY = startY + (3 * 18);
        context.fill(startX, lastSlotY + 17, startX + 18, lastSlotY + 18, 0xFF373737);
    }
}