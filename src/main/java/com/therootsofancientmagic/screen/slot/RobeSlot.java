package com.therootsofancientmagic.screen.slot;

import com.therootsofancientmagic.item.ModItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class RobeSlot extends Slot {

    public RobeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(ModItem.WEED_ROBE);
    }
}