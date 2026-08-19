package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.necklace.NecklaceItem;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow
    private int foodLevel;

    @Inject(method = "update", at = @At("HEAD"))
    private void updateExtraFood(PlayerEntity player, CallbackInfo ci) {
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
                if (this.foodLevel < 22) {
                    this.foodLevel = 22;
                }
            } else {
                if (this.foodLevel > 20) {
                    this.foodLevel = 20;
                }
            }
        }
    }
}