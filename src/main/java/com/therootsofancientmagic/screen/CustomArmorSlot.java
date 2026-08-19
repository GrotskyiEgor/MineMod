package com.therootsofancientmagic.screen;

import com.therootsofancientmagic.item.robe.RobeItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class CustomArmorSlot extends Slot {
    private final EquipmentSlot equipmentSlot;

    public CustomArmorSlot(Inventory inventory, int index, int x, int y, EquipmentSlot equipmentSlot) {
        super(inventory, index, x, y);
        this.equipmentSlot = equipmentSlot;
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof RobeItem;
    }
}