// package com.therootsofancientmagic.item.pickaxe;

// import com.therootsofancientmagic.item.ModToolMaturial;

// import net.minecraft.block.BlockState;
// import net.minecraft.entity.EquipmentSlot;
// import net.minecraft.entity.LivingEntity;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.item.Item;
// import net.minecraft.item.ItemStack;
// import net.minecraft.item.PickaxeItem;
// import net.minecraft.item.ToolMaterial;
// import net.minecraft.server.network.ServerPlayerEntity;
// import net.minecraft.util.hit.BlockHitResult;
// import net.minecraft.util.hit.HitResult;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.util.math.Direction;
// import net.minecraft.world.World;

// public class ElementalPickaxeItem extends PickaxeItem {

//     public ElementalPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
//         super(material, attackDamage, attackSpeed, settings);
//     }

//     @Override
//     public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
//         // Only run code on the logical server and if a real player mined the block
//         if (!world.isClient() && miner instanceof ServerPlayerEntity player) {
            
//             // 1. WEED ABILITY (1x1x3 mining)
//             if (this.getMaterial() == ModToolMaturial.ESSENCE_WEED && isSuitableFor(state)) {

//                 //breakIn3x3(world, pos, player, stack);
//             }
            
//             // 2. FIRE ABILITY (auto smelt)
//             if (this.getMaterial() == ModToolMaturial.ESSENCE_FIRE) {
//                 // Add fire logic here later
//             }
            
//             // 3. AQUA ABILITY (1x1x3 mining)
//             if (this.getMaterial() == ModToolMaturial.ESSENCE_AQUA) {
//                 // Add aqua logic here later
//             }
            
//             // 4. DARK ABILITY (4x4 mining)
//             if (this.getMaterial() == ModToolMaturial.ESSENCE_DARK) {
//                 breakArea(world, pos, player, stack, -1, 2, -1, 2);
//             }

//             // 5. LIGHT ABILITY (4x4 mining)
//             if (this.getMaterial() == ModToolMaturial.ESSENCE_LIGHT) {
//                 breakArea(world, pos, player, stack, -1, 2, -1, 2);
//             }

//             // 6. EARTH ABILITY (3x3 mining)
//             if (this.getMaterial() == ModToolMaturial.ESSENCE_EARTH) {
//                 breakArea(world, pos, player, stack, -1, 1, -1, 1);
//             }
//         }

//         return super.postMine(stack, world, state, pos, miner);
//     }
//     //Функция которая создает лазер из голови игрока и определяет на какой блок игрок смотрит
//     private Direction getPlayerDirection(ServerPlayerEntity player) {
//         HitResult hit = player.raycast(20.0D, 0.0F, false);
//         if (hit.getType() == HitResult.Type.BLOCK) {
//             return ((BlockHitResult) hit).getSide();
//         }
//         return Direction.UP;
//     }
//     //функция которая определяет куда смотрит игрок,верх/вниз/влево/вправо и в зависимости от этого ломает блоки вокруг блока на который игрок нажал
//     private BlockPos getTargetBlockPos(BlockPos center, Direction face,int gridX, int grixY) {
//         if (face == Direction.UP || face == Direction.DOWN) {
//             return center.add(gridX, 0, grixY);
//         } else if (face == Direction.EAST || face == Direction.WEST) {
//             return center.add(0, grixY, gridX);
//         } else {
//             return center.add(gridX, grixY, 0);
//         }
//     }
//     //Самая главная функция, в зависимости от значений в (), ломает блоки так,
//     // breakArea(world, pos, player, stack, -1, 1, -1, 1) - ломает блоки 3x3 
//     // breakArea(world, pos, player, stack, -1, 2, -1, 2) - ломает блоки 4x4
//     private void breakArea(World world, BlockPos targetPos, ServerPlayerEntity player, ItemStack toolStack, int minX, int maxX, int minY, int maxY, int minDepth, int maxDepth) {
//         Direction DirectionSide = getPlayerDirection(player);
//         // 3D и 2D сетка, которая определяет какие блоки ломать вокруг блока на который игрок нажал или вглубину
//         for (int depth = minDepth; depth <= maxDepth; depth++) {
//             for (int gridX = minX; gridX <= maxX; gridX++) {
//                 for (int gridY = minY; gridY <= maxY; gridY++) {

//                  // Пропускает центральный блок который игрок сломал
//                 if (depth == 0 && gridX == 0 && gridY == 0) continue;
           
//                 // Получает блоки которие возле блока на которие игрок нажал,в зависимости от стороны на которую игрок смотрит(raycast)
//               BlockPos NeighbourBlockPos = getTargetBlockPos(targetPos, DirectionSide, gridX, gridY, depth);
//               BlockState NeighbourBlockState = world.getBlockState(NeighbourBlockPos);
//             //Проверяет если блок сломать можно, если ето бедрок или воздух,то не ломает
//              if (isSuitableFor(NeighbourBlockState) && NeighbourBlockState.getHardness(world, NeighbourBlockPos) >= 0) {
//                 world.breakBlock(NeighbourBlockPos, true, player);

//                 toolStack.damage(1, player, (p) -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
//                 if (toolStack.isEmpty()) {
//                     return;
//                     }
//                 }
//             }
//         }
//     }
// }
