package com.therootsofancientmagic.item.shovel; // Убедись, что путь к пакету совпадает с твоим проектом

import com.therootsofancientmagic.item.ModToolMaturial;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import java.util.List;
import java.util.Optional;

// Ми НАСЛЕДУЕМ ShovelItem, чтоби лопата эффективно копала землю, песок и гравий
public class ElementalShovelItem extends ShovelItem {

    public ElementalShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        
        // Регистрируем собития Fabric для автоплавки Огненной лопати прямо при ее создании
        this.registerEvents();
    }

    private void registerEvents() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
                ItemStack mainHandItem = serverPlayer.getMainHandStack();

                // Проверяем, что игрок держит именно нашу Огненную лопату
                if (mainHandItem.getItem() == this && this.getMaterial() == ModToolMaturial.ESSENCE_FIRE) {
                    if (world instanceof ServerWorld serverWorld) {
                        // Запускаем переплавку блоков (например, песок превратится в стекло, глина в кирпичи)
                        if (executeAutoSmelt(serverWorld, pos, state, serverPlayer, mainHandItem)) {
                            return false; // Отменяем обичний ванильний дроп песка/земли
                        }
                    }
                }
            }
            return true;
        });
    }

    private boolean executeAutoSmelt(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player, ItemStack toolStack) {
        List<ItemStack> normalDrops = Block.getDroppedStacks(state, world, pos, null, player, player.getMainHandStack());
        boolean smeltedAny = false;

        for (ItemStack drop : normalDrops) {
            Optional<SmeltingRecipe> recipe = world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, new SimpleInventory(drop), world);

            if (recipe.isPresent()) {
                ItemStack cookedResult = recipe.get().getOutput(world.getRegistryManager()).copy();
                cookedResult.setCount(drop.getCount()); 

                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                Block.dropStack(world, pos, cookedResult);

                toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                smeltedAny = true;

                // ОГНЕННиЙ ЭФФЕКТ: 5% шанс на Огнестойкость при плавке
                if (player.getRandom().nextFloat() < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0));
                }
            }
        }
        return smeltedAny;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof ServerPlayerEntity player) {
            
            // Получаем случайное число для расчета 5% шанса баффов
            float effectChance = player.getRandom().nextFloat();
            
            // 1. WEED ABILITY (1x1x3 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_WEED) {
                breakTunnel(world, pos, player, stack, 2);
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0));
                }
            }
            
            // 3. AQUA ABILITY (1x1x3 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_AQUA) {
                breakTunnel(world, pos, player, stack, 2);
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0));
                }
            }
            
            // 4. DARK ABILITY (5x5 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK) {
                breakArea(world, pos, player, stack, -2, 2, -2, 2, 0, 0);
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
                }
            }

            // 5. LIGHT ABILITY (5x5 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT) {
                breakArea(world, pos, player, stack, -2, 2, -2, 2, 0, 0);
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
                }
            }

            // 6. EARTH ABILITY (3x3 mining)
            if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH) {
                breakArea(world, pos, player, stack, -1, 1, -1, 1, 0, 0);
                if (effectChance < 0.05F) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0));
                }
            }
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    private Direction getPlayerDirection(ServerPlayerEntity player) {
        HitResult hit = player.raycast(20.0D, 0.0F, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) hit).getSide();
        }
        return Direction.UP;
    }

    private void breakTunnel(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack toolStack, int maxDepth) {
        Direction face = getPlayerDirection(player);
        Direction inwardDirection = face.getOpposite();

        for (int i = 1; i <= maxDepth; i++) {
            BlockPos nextBlockPos = targetPos.offset(inwardDirection, i);
            BlockState nextBlockState = world.getBlockState(nextBlockPos);

            // Перевіряємо, чи блок підходить для копання лопатою (земля, пісок)
            if (this.isSuitableFor(nextBlockState) && nextBlockState.getHardness(world, nextBlockPos) >= 0) {
                world.breakBlock(nextBlockPos, true, player);
                toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                if (toolStack.isEmpty()) return;
            }
        }
    }

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

    private void breakArea(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack toolStack, int minX, int maxX, int minY, int maxY, int minDepth, int maxDepth) {
        Direction directionSide = getPlayerDirection(player);
        
        for (int depth = minDepth; depth <= maxDepth; depth++) {
            for (int gridX = minX; gridX <= maxX; gridX++) {
                for (int gridY = minY; gridY <= maxY; gridY++) {

                    if (depth == 0 && gridX == 0 && gridY == 0) continue;
               
                    BlockPos neighbourBlockPos = getTargetBlockPos(targetPos, directionSide, gridX, gridY, depth);
                    BlockState neighbourBlockState = world.getBlockState(neighbourBlockPos);
                
                    // Перевіряємо за допомогою лопати, щоб не ламати камінь великою областю
                    if (this.isSuitableFor(neighbourBlockState) && neighbourBlockState.getHardness(world, neighbourBlockPos) >= 0) {
                        world.breakBlock(neighbourBlockPos, true, player);
                        toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                        if (toolStack.isEmpty()) return;
                    }
                }
            }
        }
    }
}
