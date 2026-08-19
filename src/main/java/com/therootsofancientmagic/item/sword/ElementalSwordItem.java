package com.therootsofancientmagic.item.sword;

import com.therootsofancientmagic.item.ModToolMaturial;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class ElementalSwordItem extends SwordItem {

    public ElementalSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Случайный шанс, 0.3f = 30% шанс
        float randomChance = attacker.getRandom().nextFloat();

        // Проверяем, что в руках огненный меч и случайно срабатывает эффект поджигания с шансом 30% 
        if (this.getMaterial() == ModToolMaturial.ESSENCE_FIRE && randomChance < 0.3F) {
            target.setOnFireFor(5); // Поджигаем цель на 5 секунд
        }
        // Проверяем, что в руках водный меч и случайно срабатывает эффект медлительности с шансом 30%
        if (this.getMaterial() == ModToolMaturial.ESSENCE_AQUA && randomChance < 0.3F) {
            // Медлительность II уровня на 4 секунды
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
        }
        // Проверяем что рукав в руках воздушний меч и случайно срабатывает эффект отбрасывания с шансом 30%
        if (this.getMaterial() == ModToolMaturial.ESSENCE_WEED && randomChance < 0.3F) {
            // Мы передаем направление взгляда игрока(как raycast)
            target.takeKnockback(1.2F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
         if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH && randomChance < 0.3F) {
            // Подбрасываем цель 5 блоков верх
            target.addVelocity(0, 1.0, 0);
            target.velocityModified = true;
        }
        
        // Проверяем, что в руках темный меч и случайно срабатывает эффект слепоти с шансом 30%
        if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK && randomChance < 0.3F) {
            // Слепота I уровня на 5 секунд 
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
        }
        // Проверяем, что в руках светлый меч и случайно срабатывает эффект свечения с шансом 30%
        if (this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT && randomChance < 0.3F) {
            // Свечение I уровня на 7 секунд
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 140, 0));
        }

        return super.postHit(stack, target, attacker);
    }
}
