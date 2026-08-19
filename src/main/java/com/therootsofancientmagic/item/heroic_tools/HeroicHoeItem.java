package com.therootsofancientmagic.item.heroic_tools;

import com.therootsofancientmagic.item.ModToolMaturial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.List;

public class HeroicHoeItem extends HoeItem {

    public HeroicHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
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
                // Накладивает еффект удачи на игрока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 2, 0, false, false, true));
                // Добавляет игроку +1 сердце, как при поедании золотого яблока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60, 0, false, false, true));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}