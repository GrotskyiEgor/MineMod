package com.therootsofancientmagic.item.armor.boots;

import com.therootsofancientmagic.item.armor.ModArmorMaterial;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FireBoots extends ArmorItem {

    public FireBoots(Item.Settings settings) {
        super(
                ModArmorMaterial.ESSENCE_FIRE,
                ArmorItem.Type.BOOTS,
                settings
        );
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            World world,
            net.minecraft.entity.Entity entity,
            int slot,
            boolean selected
    ) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClient && entity instanceof LivingEntity player) {

            if (player.getEquippedStack(this.getSlotType()).isOf(this)) {

                player.addStatusEffect(
                        new StatusEffectInstance(
                                StatusEffects.FIRE_RESISTANCE,
                                40,
                                0,
                                false,
                                false,
                                false
                        )
                );
            }
        }
    }
}