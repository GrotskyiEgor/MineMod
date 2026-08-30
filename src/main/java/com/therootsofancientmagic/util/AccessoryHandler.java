package com.therootsofancientmagic.util;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.magic.necklace.NecklaceItem;
import com.therootsofancientmagic.item.magic.ring.RingItem;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class AccessoryHandler {

    public static void applyAccessoryEffects(ServerPlayerEntity player) {
        if (!(player instanceof CustomArmorHolder holder)) return;
        Inventory inv = holder.getCustomArmorInventory();

        ItemStack necklaceStack = inv.getStack(1);
        if (!necklaceStack.isEmpty() && necklaceStack.getItem() instanceof NecklaceItem) {
            if (player.getHungerManager().getFoodLevel() < 20) {
                player.getHungerManager().setFoodLevel(20);
                player.getHungerManager().setSaturationLevel(5.0f);
            }
        }

        EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            boolean hasRing1 = inv.getStack(2).getItem() instanceof RingItem;
            boolean hasRing2 = inv.getStack(3).getItem() instanceof RingItem;

            boolean containsModifier1 = maxHealth.getModifier(RingItem.RING_1_MODIFIER_ID) != null;
            if (hasRing1 && !containsModifier1) {
                maxHealth.addTemporaryModifier(new EntityAttributeModifier(
                        RingItem.RING_1_MODIFIER_ID, "Ring Health 1", 4.0, EntityAttributeModifier.Operation.ADDITION));
            } else if (!hasRing1 && containsModifier1) {
                maxHealth.removeModifier(RingItem.RING_1_MODIFIER_ID);
            }

            boolean containsModifier2 = maxHealth.getModifier(RingItem.RING_2_MODIFIER_ID) != null;
            if (hasRing2 && !containsModifier2) {
                maxHealth.addTemporaryModifier(new EntityAttributeModifier(
                        RingItem.RING_2_MODIFIER_ID, "Ring Health 2", 4.0, EntityAttributeModifier.Operation.ADDITION));
            } else if (!hasRing2 && containsModifier2) {
                maxHealth.removeModifier(RingItem.RING_2_MODIFIER_ID);
            }

            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }
}