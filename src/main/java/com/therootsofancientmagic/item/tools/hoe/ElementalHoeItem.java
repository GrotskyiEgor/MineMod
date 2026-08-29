package com.therootsofancientmagic.item.tools.hoe;

import com.therootsofancientmagic.item.ModToolMaturial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EquipmentSlot;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import java.util.List;

public class ElementalHoeItem extends HoeItem {

    public ElementalHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
    // Главный метод, который срабатывает, когда игрок нажимает правой кнопкой мыши по любому блоку
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos clickedPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack hoeStack = context.getStack();
        BlockState clickedState = world.getBlockState(clickedPos);

        // Проверяет, нажал ли игрок на грядку с выросшим растением
        if (clickedState.getBlock() instanceof CropBlock clickedCrop && clickedCrop.isMature(clickedState)) {
            if (world.isClient()) { 
                return ActionResult.SUCCESS;
            }
            if (player instanceof ServerPlayerEntity serverPlayer) {
                Item seedItem = clickedCrop.getPickStack(world, clickedPos, clickedState).getItem();
                
                // Получаем случайное число для расчета 5% шанса баффов
                float effectChance = player.getRandom().nextFloat();
                if (effectChance < 0.05F) {
                    applyElementalBuff(serverPlayer);
                }

                // Смотрит на материал мотиги и вибирает нужний сбор, типо если aqua мотига то 3 блока вперед собирает, если огненная то 3 на 3
                if (this.getMaterial() == ModToolMaturial.ESSENCE_AIR || this.getMaterial() == ModToolMaturial.ESSENCE_AQUA) {
                    Direction playerFacing = serverPlayer.getHorizontalFacing();

                    // Собирает по прямой линии на 3 блока вперед
                    for (int i = 0; i < 3; i++) {
                        // Если мотига сломалась то цикл прекращаеться
                        if (hoeStack.isEmpty()) break;

                        BlockPos nextCropPos = clickedPos.offset(playerFacing, i);
                        if (!tryHarvestAndReplant(world, nextCropPos, serverPlayer, hoeStack)) break;
                    }
                }
                // Огненная и Земляная, собирает 3 на 3
                else if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH || this.getMaterial() == ModToolMaturial.ESSENCE_FIRE) {
                    harvestAndReplantArea(world, clickedPos, serverPlayer, hoeStack, -1, 1, -1, 1);
                }
                // Дарк и Лайт, собирает 5 на 5
                else if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK || this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT) {
                    harvestAndReplantArea(world, clickedPos, serverPlayer, hoeStack, -2, 2, -2, 2);
                }
                return ActionResult.CONSUME;
            }
        }
        return super.useOnBlock(context);
    }

    // Налаживаем ефект в зависимости от мотиги
    private void applyElementalBuff(ServerPlayerEntity player) {
        if (this.getMaterial() == ModToolMaturial.ESSENCE_AIR) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0));
        } else if (this.getMaterial() == ModToolMaturial.ESSENCE_AQUA) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0));
        } else if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK || this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
        } else if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0));
        } else if (this.getMaterial() == ModToolMaturial.ESSENCE_FIRE) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0));
        }
    }
    // обрабатывает один конкретный блок, типо (сбор плодов + посадка семечка обратно)
    private boolean tryHarvestAndReplant(World world, BlockPos pos, ServerPlayerEntity player, ItemStack hoeStack) {
        BlockState state = world.getBlockState(pos);
        
        // Опять проверяет что перед нами созревший урожай
        if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(state)) {
            Item seedItem = cropBlock.getPickStack(world, pos, state).getItem();
            
            // Проверяет если ли игрок в креативе, если нет то ищет семечко в инвентаре
           if (!player.isCreative() && player.getInventory().count(seedItem) <= 0) {
                return false;
            }
                // Считает лут, который должен был выпасть если би ми все просто собирали 
                List<ItemStack> drops = Block.getDroppedStacks(state, (net.minecraft.server.world.ServerWorld) world, pos, null, player, hoeStack);
                    
                for (ItemStack drop : drops) {
                    Block.dropStack(world, pos, drop);
                }
                // Забирает семена из инвентаре при посадке
                if (!player.isCreative()) {
                    player.getInventory().remove(stack -> stack.getItem() == seedItem, 1, player.getInventory());
                }
                world.setBlockState(pos, cropBlock.withAge(0), 3);

                // Тратит 1 прочность мотиги
                hoeStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
            return true;
        }
    

    //Перебор координатной сетки
    private void harvestAndReplantArea(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack hoeStack, int minX, int maxX, int minZ, int maxZ) {
        for (int gridX = minX; gridX <= maxX; gridX++) {
            for (int gridZ = minZ; gridZ <= maxZ; gridZ++) {
                // Останавливает цикл при ломании мотиги
                if (hoeStack.isEmpty()) return;
                // Находит соседний блок
                BlockPos neighbourCropPos = targetPos.add(gridX, 0, gridZ);
                // Собирает и пересаживает
                if (!tryHarvestAndReplant(world, neighbourCropPos, player, hoeStack)) {
                    return;
                }
            }
        }
    }
}