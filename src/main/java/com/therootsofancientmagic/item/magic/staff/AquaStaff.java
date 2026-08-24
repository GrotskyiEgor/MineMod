package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana; // Импортируем нашу систему мани
import com.therootsofancientmagic.util.IEntityDataSaver; // Импортируем кармашек NBT данних
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity; // Добавили серверного игрока для пакетов
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class AquaStaff extends Item {

    private static final int COOLDOWN_TICKS = 15;

    // Размер конструкции: 5x5x5
    private static final int SIZE = 5;
    private static final int HALF_SIZE = SIZE / 2;

    // Максимальная дальность луча
    private static final double RAY_RANGE = 20.0D;

    public AquaStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {

        ItemStack stack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            if (user instanceof ServerPlayerEntity serverPlayer) {
                
                if (PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
                    
                    createIceDome(world, user);

                    world.playSound(
                            null,
                            user.getBlockPos(),
                            SoundEvents.BLOCK_GLASS_PLACE,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.0F
                    );

                    user.getItemCooldownManager().set(
                            this,
                            COOLDOWN_TICKS
                    );

                } else {
                    return TypedActionResult.fail(stack);
                }
            }
        }

        return TypedActionResult.success(
                stack,
                world.isClient
        );
    }

    private void createIceDome(
            World world,
            PlayerEntity user
    ) {

        BlockPos bottomPos = getTargetPoint(
                world,
                user
        );

        BlockPos centerPos = bottomPos.up(HALF_SIZE);

        for (int x = -HALF_SIZE; x <= HALF_SIZE; x++) {

            for (int y = -HALF_SIZE; y <= HALF_SIZE; y++) {

                for (int z = -HALF_SIZE; z <= HALF_SIZE; z++) {

                    BlockPos blockPos = centerPos.add(
                            x,
                            y,
                            z
                    );

                    if (!world.getBlockState(blockPos).isReplaceable()) {
                        continue;
                    }

                    boolean isOuterLayer =
                            Math.abs(x) == HALF_SIZE
                            || Math.abs(y) == HALF_SIZE
                            || Math.abs(z) == HALF_SIZE;

                    if (isOuterLayer) {
                        world.setBlockState(
                                blockPos,
                                Blocks.ICE.getDefaultState()
                        );

                    } else {
                        world.setBlockState(
                                blockPos,
                                Blocks.WATER.getDefaultState()
                        );
                    }
                }
            }
        }
    }

    private BlockPos getTargetPoint(
            World world,
            PlayerEntity player
    ) {

        Vec3d start = player.getCameraPosVec(1.0F);

        Vec3d direction = player.getRotationVec(1.0F);

        Vec3d end = start.add(
                direction.multiply(RAY_RANGE)
        );

        BlockHitResult hitResult = world.raycast(
                new RaycastContext(
                        start,
                        end,
                        RaycastContext.ShapeType.OUTLINE,
                        RaycastContext.FluidHandling.NONE,
                        player
                )
        );

        if (hitResult.getType() == HitResult.Type.BLOCK) {

            return BlockPos.ofFloored(
                    hitResult.getPos()
            );
        }

        return BlockPos.ofFloored(end);
    }
}
