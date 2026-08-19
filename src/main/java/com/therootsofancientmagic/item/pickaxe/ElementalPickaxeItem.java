package com.therootsofancientmagic.item.pickaxe;

import com.therootsofancientmagic.item.ModToolMaturial;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ElementalPickaxeItem extends PickaxeItem {

    public ElementalPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        this.registerEvents();
    }
    private void registerEvents() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
                ItemStack mainHandItem = serverPlayer.getMainHandStack();

                // Проверяет если игрок держит в руке кирку с материалом ESSENCE_FIRE, то активирует функцию AutoSmeltBlock
                if (mainHandItem.getItem() == this && this.getMaterial() == ModToolMaturial.ESSENCE_FIRE) {
                    
                    //Визиваем метод AutoSmeltBlock, которий обрабативает переплавку
                    if (AutoSmeltBlock((ServerWorld) world, pos, state, serverPlayer, mainHandItem)) {
                        //Если автоплавка сработала, отменяем випадение непереплавленних предметов
                        return false; 
                    }
                }
            }
            return true;
        });
    }
    
    // Функция, которая переплавляет блоки, если на них есть рецепт переплавки,например железная руда сразу превращаеться железний слиток
    private boolean AutoSmeltBlock(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player, ItemStack toolStack) {
        // Получаем список предметов, которие должни били випасть
        List<ItemStack> normalDrops = Block.getDroppedStacks(state, world, pos, null, player, player.getMainHandStack());
        boolean smeltedAny = false;

        for (ItemStack drop : normalDrops) {
            // Ищем подходящий рецепт плавки в печи для полученного предмета
            Optional<SmeltingRecipe> recipe = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(drop), world);

            // Если нашли рецепт, то сразу переплавляет предмет в его переплавленний предмет,например железная руда переплавляеться в железний слиток
            if (recipe.isPresent()) {
                ItemStack cookedResult = recipe.get().getOutput(world.getRegistryManager()).copy();
                cookedResult.setCount(drop.getCount());

                // Удаляет старий предмет из мира и заменяет его на переплавленний предмет
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                Block.dropStack(world, pos, cookedResult);
                toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                smeltedAny = true;
                // 5% шанс на накладивание эффекта Огнестойкость I на 30 секунд при ломание блока огненной киркой
                if (player.getRandom().nextFloat() < 0.05F) {
                    // Накладиваем Огнестойкость I на 30 секунд
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0));
                }
            }
        }
        return smeltedAny;
    }
    
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof ServerPlayerEntity player) {
            // Генерируем случайное число от 0.0 до 1.0 для шанса на наложение эффекта, если видает 0.05(5%),то накладивает эффект
            float effectChance = player.getRandom().nextFloat();

            // 1. WEED ABILITY (1x1x3 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_WEED && isSuitableFor(state)) {
                breakTunnel(world, pos, player, stack, 2);
                // 5% шанс на накладивание эффекта Скорость I на 30 секунд при ломание блока воздушной киркой
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0));
                }
            }
            
            // 3. AQUA ABILITY (1x1x3 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_AQUA) {
                breakTunnel(world, pos, player, stack, 2);
                // 5% шанс на накладивание эффекта Водное дихание I на 30 секунд при ломание блока водной киркой
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0));
                }
            }
            
            // 4. DARK ABILITY (5x5 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK) {
                breakArea(world, pos, player, stack, -2, 2, -2, 2, 0, 0);
                // 5% шанс на накладивание эффекта Ночного зрения I на 30 секунд при ломание блока темной киркой
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
                }

            }

            // 5. LIGHT ABILITY (5x5 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT) {
                breakArea(world, pos, player, stack, -2, 2, -2, 2, 0, 0);
                // 5% шанс на накладивание эффекта Ночного зрения I на 30 секунд при ломание блока светлой киркой
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
                }
            }

            // 6. EARTH ABILITY (3x3 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH) {
                breakArea(world, pos, player, stack, -1, 1, -1, 1, 0, 0);
                // 5% шанс на накладивание эффекта Ускорение I на 30 секунд при ломание блока земной киркой
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0));
                }
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }
    //Функция которая создает лазер из голови игрока и определяет на какой блок игрок смотрит
    private Direction getPlayerDirection(ServerPlayerEntity player) {
        HitResult hit = player.raycast(20.0D, 0.0F, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hit).getSide();
        }
        return Direction.UP;
    }
    //функция которая определяет куда смотрит игрок,верх/вниз/влево/вправо и в зависимости от этого ломает блоки вокруг блока на которий игрок нажал
    private BlockPos getTargetBlockPos(BlockPos center, Direction face, int gridX, int grixY, int depth) {
        BlockPos shiftedCenter = center.offset(face.getOpposite(), depth);

        if (face == Direction.UP || face == Direction.DOWN) {
            return shiftedCenter.add(gridX, depth, grixY);
        } else if (face == Direction.EAST || face == Direction.WEST) {
            return shiftedCenter.add(depth, grixY, gridX);
        } else {
            return shiftedCenter.add(gridX, grixY, depth);
        }
    }
    //Тоже самое что и breakArea, только ломает блоки вглубь, в зависимости от того на какой блок игрок нажал
    private void breakTunnel(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack toolStack, int maxDepth) {
        Direction face = getPlayerDirection(player);
        Direction inwardDirection = face.getOpposite();

        for (int i = 1; i <= maxDepth; i++) {
            BlockPos nextBlockPos = targetPos.offset(inwardDirection, i);
            BlockState nextBlockState = world.getBlockState(nextBlockPos);

            if (!nextBlockState.isAir() && nextBlockState.getHardness(world, nextBlockPos) >= 0) {
                world.breakBlock(nextBlockPos, true, player);
                toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                if (toolStack.isEmpty()) {
                    return;
                }
            }
        }
    }
    //Самая главная функция, в зависимости от значений в (), ломает блоки так,
    // breakArea(world, pos, player, stack, -1, 1, -1, 1) - ломает блоки 3x3 
    // breakArea(world, pos, player, stack, -1, 2, -1, 2) - ломает блоки 4x4
    private void breakArea(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack toolStack, int minX, int maxX, int minY, int maxY, int minDepth, int maxDepth) {
        Direction DirectionSide = getPlayerDirection(player);
        // 3D и 2D сетка, которая определяет какие блоки ломать вокруг блока на которий игрок нажал или вглубину
        for (int depth = minDepth; depth <= maxDepth; depth++) {
            for (int gridX = minX; gridX <= maxX; gridX++) {
                for (int gridY = minY; gridY <= maxY; gridY++) {

                 // Пропускает центральний блок которий игрок сломал
                if (depth == 0 && gridX == 0 && gridY == 0) continue;
           
              // Получает блоки которие возле блока на которий игрок нажал,в зависимости от сторони на которую игрок смотрит(raycast)
              BlockPos NeighbourBlockPos = getTargetBlockPos(targetPos, DirectionSide, gridX, gridY, depth);
              BlockState NeighbourBlockState = world.getBlockState(NeighbourBlockPos);
             //Проверяет если блок сломать можно, если ето бедрок или воздух,то не ломает
             if (!NeighbourBlockState.isAir() && NeighbourBlockState.getHardness(world, NeighbourBlockPos) >= 0) {
                        world.breakBlock(NeighbourBlockPos, true, player);

                        toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                        if (toolStack.isEmpty()) {
                            return;
                }
            }
        }
    }
}
}
}