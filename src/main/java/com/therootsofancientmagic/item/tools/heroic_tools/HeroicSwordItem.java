package com.therootsofancientmagic.item.tools.heroic_tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class HeroicSwordItem extends SwordItem {

    public HeroicSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof LivingEntity livingEntity) { 

            // Проверяет, держит ли игрок героический топор в руке
            boolean isHoldingInMainHand = selected; 
            boolean isHoldingInOffHand = livingEntity.getOffHandStack() == stack; 

            // Проверяет, держит ли вобще меч в любой руке
            if (isHoldingInMainHand || isHoldingInOffHand) { 
                // Накладивает Силу 2 на игрока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 2, 1, false, false, true)); 
                
                // Накладивает Огненний Резистенс(стойкость к огню) 2 на игрока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 2, 1, false, false, true));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected); 
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient()) {
            // Поджигает цель на 5 секунд
            target.setOnFireFor(5);
            
            // Накладивает замедление 1 на врага при ударе
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 0));
        }
        return super.postHit(stack, target, attacker);
    }
}
