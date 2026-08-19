package com.therootsofancientmagic.item.heroic_tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeroicAxeItem extends AxeItem {

    public HeroicAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient()) {
            
            // проверка на крит удар, оно проверяет что игрок не на земле и не лазит по лестнице
            boolean isCritical = attacker.getVelocity().y < 0 
                    && !attacker.isOnGround() 
                    && !attacker.isClimbing();

            if (isCritical) {
                // При критическом ударе станит врага на 3 секунди
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 4));
            } else {
                // Подбрасует врага при обичном ударе как у земляного меча
                target.addVelocity(0, 1.0, 0);
                target.velocityModified = true; 
            }
        }

        return super.postHit(stack, target, attacker);
    }
@Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof LivingEntity livingEntity) { 

            // Проверяет, держит ли игрок героический топор в руке
            boolean isHoldingInMainHand = selected; 
            boolean isHoldingInOffHand = livingEntity.getOffHandStack() == stack; 

            // Проверяет, держит ли вобще топор в любой руке
            if (isHoldingInMainHand || isHoldingInOffHand) { 
                // Накладивает Скорость 2 на игрока 
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2, 1, false, false, true)); 
                
                // Накладивает Резистенс(стойкость) 2 на игрока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, 1, false, false, true));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected); 
    }

@Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof ServerPlayerEntity player) {
            
            //Проверяет, является ли блок деревом
            if (state.isIn(BlockTags.LOGS)) {
                // Проходит по дереву вверх, начиная с блока, которий бил сломан, просто чтоби найти все блоки дерева в пределах 30 блоков вверх
                for (int y = 1; y <= 30; y++) {
                    // Если топор сломался то дерево перестает ломаться
                    if (stack.isEmpty()) break; 

                    boolean foundLogInLayer = false;
                    // Определяет центр дерева, которий ми проверяем
                    BlockPos layerCenter = pos.up(y);
                    
                    // Сетка 5x5 вокруг центра дерева,чтоби найти все блоки дерева в пределах этого слоя
                     for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            // Получает точние координати конкретного блока на этой сетке 3х3
                            BlockPos nextLogPos = layerCenter.add(x, 0, z);
                            // Получает состояние блока на этих координатах
                            BlockState nextLogState = world.getBlockState(nextLogPos);

                           // Проверяет является ли этот блок деревом,если да, то ломает его и наносит урон топору
                            if (nextLogState.isIn(BlockTags.LOGS)) {
                                world.breakBlock(nextLogPos, true, player);
                                stack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                                // Если блок дерева сломали, то дает значение True
                                foundLogInLayer = true;
                            }
                        }
                    }
                    // Если дерева верху больше нету, например небо началось, то цикл останавливаеться
                    if (!foundLogInLayer) {
                        break; 
                    }
                }
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }
}
