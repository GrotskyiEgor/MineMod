package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.screen.CustomArmorSlot;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {

    private static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[]{
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    protected PlayerScreenHandlerMixin() {
        super(null, 0);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomArmorSlots(PlayerInventory playerInventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        if (owner instanceof CustomArmorHolder holder) {
            Inventory customArmor = holder.getCustomArmorInventory();

            for (int i = 0; i < 4; i++) {
                this.addSlot(new CustomArmorSlot(customArmor, i, -23, 8 + i * 18, EQUIPMENT_SLOTS[i]));
            }
        }
    }

    @Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
    private void handleCustomQuickMove(PlayerEntity player, int slotIndex, CallbackInfoReturnable<ItemStack> cir) {
        if (slotIndex < 0 || slotIndex >= this.slots.size()) return;

        Slot slot = this.slots.get(slotIndex);

        if (slot instanceof CustomArmorSlot && slot.hasStack()) {
            ItemStack stackInSlot = slot.getStack();
            ItemStack copy = stackInSlot.copy();

            if (!this.insertItem(stackInSlot, 9, 45, true)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }

            if (stackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            slot.onTakeItem(player, stackInSlot);
            cir.setReturnValue(copy);
        }
    }
}