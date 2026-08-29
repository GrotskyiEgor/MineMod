package com.therootsofancientmagic.item.tools.axe; // Убедись, что путь к пакету совпадает с твоим проектом

import com.therootsofancientmagic.item.ModToolMaturial;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ElementalAxeItem extends AxeItem {

    public ElementalAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof ServerPlayerEntity player) {
            
            // Получаем случайное число для расчета 5% шанса баффов
            float effectChance = player.getRandom().nextFloat();
            if (effectChance < 0.05F) {
                if (this.getMaterial() == ModToolMaturial.ESSENCE_AIR) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0));
                }
                else if (this.getMaterial() == ModToolMaturial.ESSENCE_AQUA) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0));
                } 
                else if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK || this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
                } 
                else if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0));
                } 
                else if (this.getMaterial() == ModToolMaturial.ESSENCE_FIRE) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0));
                }
            }
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
