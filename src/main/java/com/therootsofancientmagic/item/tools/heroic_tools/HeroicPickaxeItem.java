package com.therootsofancientmagic.item.tools.heroic_tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
                // Накладывает Спешку 2 на игрока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 2, 1, false, false, true));
                // Накладивает ночное зрение на игрока
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2, 0, false, false, true));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof ServerPlayerEntity player) {
            
            if (this.isSuitableFor(state)) {
                // Ломание 5 на 5 и еще 1 блок вглубину
                this.breakAreaAndTunnel(world, pos, player, stack, -2, 2, -2, 2, 0, 1);
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }
    // Функция которая создает лазер из голови игрока и определяет на какой блок игрок смотрит
    private Direction getPlayerDirection(ServerPlayerEntity player) {
        HitResult hit = player.raycast(20.0D, 0.0F, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hit).getSide();
        }
        return Direction.UP;
    }

   //функция которая определяет куда смотрит игрок,верх/вниз/влево/вправо и в зависимости от этого ломает блоки вокруг блока на которий игрок нажал
    private BlockPos getTargetBlockPos(BlockPos center, Direction face, int gridX, int gridY, int depth) {
        
        BlockPos shiftedCenter = center.offset(face.getOpposite(), depth);

        if (face == Direction.UP || face == Direction.DOWN) {
            return shiftedCenter.add(gridX, 0, gridY);
        } else if (face == Direction.EAST || face == Direction.WEST) {
            return shiftedCenter.add(0, gridY, gridX);
        } else {
            return shiftedCenter.add(gridX, gridY, 0);
        }
    }
    // Самая главная функция зачистки: перебирает сетку и копает блоки вглубь
    private void breakAreaAndTunnel(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack toolStack, 
            int minX, int maxX, int minY, int maxY, int minDepth, int maxDepth) {
        
        Direction directionSide = getPlayerDirection(player);

        // 3D и 2D сетка, которая определяет какие блоки ломать вокруг блока на которий игрок нажал или вглубину
        for (int depth = minDepth; depth <= maxDepth; depth++) {
            for (int gridX = minX; gridX <= maxX; gridX++) {
                for (int gridY = minY; gridY <= maxY; gridY++) {

                    // Пропускает центральний блок которий игрок сломал
                    if (depth == 0 && gridX == 0 && gridY == 0) continue;

                    // Получает блоки которие возле блока на которий игрок нажал,в зависимости от сторони на которую игрок смотрит(raycast)
                    BlockPos neighbourBlockPos = getTargetBlockPos(targetPos, directionSide, gridX, gridY, depth);
                    BlockState neighbourBlockState = world.getBlockState(neighbourBlockPos);

                    //Проверяет если блок сломать можно, если ето бедрок или воздух,то не ломает
                    if (this.isSuitableFor(neighbourBlockState) && neighbourBlockState.getHardness(world, neighbourBlockPos) >= 0) {
                        world.breakBlock(neighbourBlockPos, true, player);
                        //Наносит 1 урон кирке при ломании блока
                        toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                        // Если кирка сломалась при копании, цикл перестает копать
                        if (toolStack.isEmpty()) {
                            return;
                        }
                    }
                }
            }
        }
}
}