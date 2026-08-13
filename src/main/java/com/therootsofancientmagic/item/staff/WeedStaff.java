package com.therootsofancientmagic.item.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class WeedStaff extends Item {

    // Кулдаун в тиках (20 тиков = 1 секунда)
    private static final int COOLDOWN_TICKS = 40;

    // Дистанция от игрока до центра "черной дыры"
    private static final double TARGET_DISTANCE = 5.0;

    // Радиус притяжения вокруг центра
    private static final double PULL_RADIUS = 3.0;

    // Сила притяжения
    private static final double PULL_STRENGTH = 0.6;

    public WeedStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Проверка кулдауна
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            castSpell(world, user);

            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.5F
            );
        }

        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        return TypedActionResult.success(stack, world.isClient);
    }

    private void castSpell(World world, PlayerEntity user) {
        // Направление взгляда игрока
        var lookVec = user.getRotationVec(1.0F);

        // Точка перед игроком - центр притяжения
        Vec3d center = user.getEyePos().add(
                lookVec.x * TARGET_DISTANCE,
                lookVec.y * TARGET_DISTANCE,
                lookVec.z * TARGET_DISTANCE
        );

        Box area = new Box(
                center.x - PULL_RADIUS, center.y - PULL_RADIUS, center.z - PULL_RADIUS,
                center.x + PULL_RADIUS, center.y + PULL_RADIUS, center.z + PULL_RADIUS
        );

        List<Entity> entities = world.getOtherEntities(user, area);

        for (Entity entity : entities) {
            double distance = entity.getPos().distanceTo(center);

            if (distance > PULL_RADIUS || distance < 0.1) {
                continue;
            }

            // Вектор от сущности к центру
            Vec3d pullDirection = center.subtract(entity.getPos()).normalize();

            // Чем ближе к центру, тем меньше рывок, чтобы не улетали "сквозь" точку
            double strength = PULL_STRENGTH * (distance / PULL_RADIUS);

            entity.addVelocity(
                    pullDirection.x * strength,
                    pullDirection.y * strength,
                    pullDirection.z * strength
            );

            entity.velocityModified = true;
        }
    }
}
