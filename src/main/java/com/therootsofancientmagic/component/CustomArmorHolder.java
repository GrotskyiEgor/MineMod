package com.therootsofancientmagic.component;

import net.minecraft.inventory.Inventory;

public interface CustomArmorHolder {
    Inventory getCustomArmorInventory();
    default void ActivateAquaRobeAbility() {}
    default void ActivateFireRobeAbility() {}
}