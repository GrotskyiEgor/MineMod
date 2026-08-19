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
    // Главний метод как и harvestAndreplant
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos clickedPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack hoeStack = context.getStack();
        BlockState clickedState = world.getBlockState(clickedPos);

        if (clickedState.getBlock() instanceof CropBlock clickedCrop && clickedCrop.isMature(clickedState)) {
            
            if (world.isClient()) {
                return ActionResult.SUCCESS;
            }

            if (player instanceof ServerPlayerEntity serverPlayer) {
                
                harvestAndReplantArea(world, clickedPos, serverPlayer, hoeStack, -3, 3, -3, 3);

                return ActionResult.CONSUME;
            }
        }

        return super.useOnBlock(context);
    }
        // обрабатывает один конкретный блок, типо (сбор плодов + посадка семечка обратно)
        private boolean tryHarvestAndReplant(World world, BlockPos pos, ServerPlayerEntity player, ItemStack hoeStack) { // //
        BlockState state = world.getBlockState(pos);
        
        if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(state)) {
            Item seedItem = cropBlock.getPickStack(world, pos, state).getItem();

           //Если семена кончились то преривает харвест и посадку
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

    private void harvestAndReplantArea(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack hoeStack, int minX, int maxX, int minZ, int maxZ) {
        for (int gridX = minX; gridX <= maxX; gridX++) {
            for (int gridZ = minZ; gridZ <= maxZ; gridZ++) {
                if (hoeStack.isEmpty()) return;

                BlockPos neighbourCropPos = targetPos.add(gridX, 0, gridZ);
                // Харвест 7 на 7 или столько сколько есть семян в инвентаре
                if (!tryHarvestAndReplant(world, neighbourCropPos, player, hoeStack)) {
                    return;
                }
            }
        }
    }
}