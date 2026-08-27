package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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

    // Радиус сферы (было "SIZE" для куба 5x5x5 — теперь это радиус шара)
    private static final int RADIUS = 3;

    // Толщина ледяной оболочки снаружи
    private static final double SHELL_THICKNESS = 1.2;

    private static final double RAY_RANGE = 20.0D;

    public AquaStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            if (!PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
                return TypedActionResult.fail(stack);
            }

            createIceSphere(world, user);

            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 1.0F, 1.0F);

            user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    /**
     * Создаёт шар: снаружи лёд, внутри вода.
     * Форма определяется расстоянием от центра до блока,
     * а не диапазоном по X/Y/Z — поэтому получается круглый шар,
     * а не куб.
     */
    private void createIceSphere(World world, PlayerEntity user) {
        BlockPos bottomPos = getTargetPoint(world, user);
        BlockPos centerPos = bottomPos.up(RADIUS);

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {

                    double distance = Math.sqrt(x * x + y * y + z * z);

                    // За пределами шара — пропускаем
                    if (distance > RADIUS) {
                        continue;
                    }

                    BlockPos blockPos = centerPos.add(x, y, z);

                    if (!world.getBlockState(blockPos).isReplaceable()) {
                        continue;
                    }

                    boolean isShell = distance > RADIUS - SHELL_THICKNESS;

                    world.setBlockState(
                            blockPos,
                            isShell ? Blocks.ICE.getDefaultState() : Blocks.WATER.getDefaultState()
                    );
                }
            }
        }
    }

    private BlockPos getTargetPoint(World world, PlayerEntity player) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(RAY_RANGE));

        BlockHitResult hitResult = world.raycast(new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return BlockPos.ofFloored(hitResult.getPos());
        }

        return BlockPos.ofFloored(end);
    }
}