package com.therootsofancientmagic.screen;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class CustomArmorSlot extends Slot {
    private final EquipmentSlot equipmentSlot;

    public CustomArmorSlot(Inventory inventory, int index, int x, int y, EquipmentSlot equipmentSlot) {
        super(inventory, index, x, y);
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getSlotType() == this.equipmentSlot;
        }
        return true;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}