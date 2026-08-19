package com.therootsofancientmagic.item.heroic_tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class HeroicPickaxeItem extends PickaxeItem {

    public HeroicPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof LivingEntity livingEntity) {
            
            // Проверяет, держит ли игрок героическую кирку в руке
            boolean isHoldingInMainHand = selected;
            boolean isHoldingInOffHand = livingEntity.getOffHandStack() == stack;

             // Проверяет, держит ли вобще кирку в любой руке
            if (isHoldingInMainHand || isHoldingInOffHand) {
                // Накладывает Спешку 2 на игрока на 4 секунди
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 80, 1, false, false, true));
                // Накладивает ночное зрение на игрока на 4 секунди
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 80, 0, false, false, true));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}