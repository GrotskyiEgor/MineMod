package com.therootsofancientmagic.component;

import net.minecraft.inventory.Inventory;

public interface CustomArmorHolder {
    Inventory getCustomArmorInventory();
    void useFirstRingAbility();
    void useSecondRingAbility();
    default void ActivateAquaRobeAbility() {}
    default void ActivateFireRobeAbility() {}
}