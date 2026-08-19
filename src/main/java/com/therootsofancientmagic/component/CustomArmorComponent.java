package com.therootsofancientmagic.component;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class CustomArmorComponent {
    private final SimpleInventory inventory = new SimpleInventory(4);

    public SimpleInventory getInventory() {
        return inventory;
    }

    public void readNbt(NbtCompound tag) {
        if (tag.contains("CustomArmor", 9)) {
            NbtList list = tag.getList("CustomArmor", 10);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound itemTag = list.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < inventory.size()) {
                    inventory.setStack(slot, net.minecraft.item.ItemStack.fromNbt(itemTag));
                }
            }
        }
    }

    public void writeNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                NbtCompound itemTag = new NbtCompound();
                itemTag.putByte("Slot", (byte) i);
                inventory.getStack(i).writeNbt(itemTag);
                list.add(itemTag);
            }
        }
        tag.put("CustomArmor", list);
    }
}