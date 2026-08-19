package com.therootsofancientmagic.screen;

import com.therootsofancientmagic.item.necklace.NecklaceItem;
import com.therootsofancientmagic.item.ring.RingItem;
import com.therootsofancientmagic.item.robe.RobeItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class CustomArmorSlot extends Slot {
    private final EquipmentSlot equipmentSlot;

    public static final String MOD_ID = "the-roots-of-ancient-magic";

    public static final Identifier EMPTY_ROBE_SLOT_TEXTURE = new Identifier(MOD_ID, "item/empty_slot_robe");
    public static final Identifier EMPTY_NECKLACE_SLOT_TEXTURE = new Identifier(MOD_ID, "item/empty_slot_necklace");
    public static final Identifier EMPTY_RING_SLOT_TEXTURE = new Identifier(MOD_ID, "item/empty_slot_ring");

    public CustomArmorSlot(Inventory inventory, int index, int x, int y, EquipmentSlot equipmentSlot) {
        super(inventory, index, x, y);
        this.equipmentSlot = equipmentSlot;
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        int index = this.getIndex();

        if (index == 0) return stack.getItem() instanceof RobeItem;
        if (index == 1) return stack.getItem() instanceof NecklaceItem;
        if (index == 2 || index == 3) return stack.getItem() instanceof RingItem;

        return false;
    }

    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        int index = this.getIndex();

        if (index == 0) return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ROBE_SLOT_TEXTURE);
        if (index == 1) return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_NECKLACE_SLOT_TEXTURE);
        if (index == 2 || index == 3) return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_RING_SLOT_TEXTURE);

        return super.getBackgroundSprite();
    }
}