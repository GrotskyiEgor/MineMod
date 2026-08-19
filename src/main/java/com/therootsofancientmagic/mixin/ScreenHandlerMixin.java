package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.screen.CustomArmorSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Inject(
        method = "onSlotClick", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void handleCustomSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        ScreenHandler handler = (ScreenHandler) (Object) this;

        if (handler instanceof PlayerScreenHandler) {
            if (slotIndex >= 0 && slotIndex < handler.slots.size()) {
                Slot slot = handler.slots.get(slotIndex);

                if (slot instanceof CustomArmorSlot) {
                    if (actionType == SlotActionType.PICKUP) {
                        ItemStack cursorStack = handler.getCursorStack();
                        ItemStack slotStack = slot.getStack();

                        if (cursorStack.isEmpty() && !slotStack.isEmpty()) {
                            handler.setCursorStack(slotStack.copy());
                            slot.setStack(ItemStack.EMPTY);
                            slot.onTakeItem(player, slotStack);
                        } else if (!cursorStack.isEmpty() && slot.canInsert(cursorStack)) {
                            if (slotStack.isEmpty()) {
                                slot.setStack(cursorStack.split(1));
                            } else if (ItemStack.canCombine(slotStack, cursorStack)) {
                                int max = Math.min(cursorStack.getMaxCount(), slot.getMaxItemCount());
                                int transfer = Math.min(cursorStack.getCount(), max - slotStack.getCount());
                                cursorStack.decrement(transfer);
                                slotStack.increment(transfer);
                            }
                        }
                    } else if (actionType == SlotActionType.QUICK_MOVE) {
                        handler.quickMove(player, slotIndex);
                    } else if (actionType == SlotActionType.THROW && slot.hasStack()) {
                        ItemStack itemStack = slot.takeStack(button == 0 ? 1 : slot.getStack().getCount());
                        slot.onTakeItem(player, itemStack);
                        player.dropItem(itemStack, true);
                    }

                    slot.markDirty();
                    handler.sendContentUpdates();
                    ci.cancel();
                }
            }
        }
    }
}