package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class LightStaff extends Item {

    private static final int MANA_COST = 10;
    private static final int COOLDOWN_TICKS = 15;
    private static final double MAX_DISTANCE = 50.0;

    public LightStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity player,
            Hand hand) {

        ItemStack stack = player.getStackInHand(hand);

        // Кулдаун
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        // Только сервер
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {

            // Тратим 10 маны
            if (!PlayerMana.consumeMana(
                    (IEntityDataSaver) serverPlayer,
                    MANA_COST,
                    serverPlayer)) {

                return TypedActionResult.fail(stack);
            }

            // Откуда начинаем смотреть
            Vec3d start = serverPlayer.getCameraPosVec(1.0F);

            // Направление взгляда
            Vec3d direction = serverPlayer.getRotationVec(1.0F);

            // Максимальная точка — 50 блоков
            Vec3d end = start.add(
                    direction.x * MAX_DISTANCE,
                    direction.y * MAX_DISTANCE,
                    direction.z * MAX_DISTANCE
            );

            // Проверяем, во что смотрит игрок
            HitResult hit = world.raycast(new RaycastContext(
                    start,
                    end,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    serverPlayer
            ));

            Vec3d teleportPos;

            if (hit.getType() == HitResult.Type.BLOCK) {

                // Если смотрим в блок —
                // телепортируемся прямо перед ним
                teleportPos = hit.getPos().subtract(
                        direction.x,
                        direction.y,
                        direction.z
                );

            } else {

                // Если блоков нет —
                // телепортируемся на 50 блоков
                teleportPos = end;
            }

            // Телепортация
            serverPlayer.teleport(
                    serverPlayer.getServerWorld(),
                    teleportPos.x,
                    teleportPos.y,
                    teleportPos.z,
                    serverPlayer.getYaw(),
                    serverPlayer.getPitch()
            );

            // Звук
            world.playSound(
                    null,
                    serverPlayer.getBlockPos(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );

            // Кулдаун
            serverPlayer.getItemCooldownManager().set(
                    this,
                    COOLDOWN_TICKS
            );
        }

        return TypedActionResult.success(
                stack,
                world.isClient
        );
    }
}