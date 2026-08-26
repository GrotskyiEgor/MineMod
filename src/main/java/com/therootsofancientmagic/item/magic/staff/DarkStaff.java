package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DarkStaff extends Item {

    private static final int COOLDOWN_TICKS = 30;

    public DarkStaff(Settings settings) {
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

            // Проверяем, что игрок является серверным игроком
            if (user instanceof ServerPlayerEntity serverPlayer) {

                // Тратим 10 маны
                if (PlayerMana.consumeMana(
                        (IEntityDataSaver) serverPlayer,
                        10,
                        serverPlayer
                )) {

                    // Маны хватило — призываем двух скелетов
                    castSpell(world, user);

                    // Звук призыва
                    world.playSound(
                            null,
                            user.getBlockPos(),
                            SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT,
                            SoundCategory.PLAYERS,
                            1.0F,
                            0.7F
                    );

                    // Кулдаун
                    user.getItemCooldownManager().set(
                            this,
                            COOLDOWN_TICKS
                    );

                } else {

                    // Недостаточно маны
                    return TypedActionResult.fail(stack);
                }
            }
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    private void castSpell(World world, PlayerEntity user) {

        // Направление взгляда игрока
        var lookVec = user.getRotationVec(1.0F);

        /*
         * Вектор "вправо" относительно игрока.
         *
         * Если направление взгляда = (x, y, z),
         * то вектор вправо примерно:
         *
         * (-z, 0, x)
         */
        double rightX = -lookVec.z;
        double rightZ = lookVec.x;

        // Расстояние от игрока до скелетов
        double distance = 1.5;

        // =========================
        // СКЕЛЕТ СЛЕВА
        // =========================

        WitherSkeletonEntity leftSkeleton =
                new WitherSkeletonEntity(
                        net.minecraft.entity.EntityType.WITHER_SKELETON,
                        world
                );

        leftSkeleton.setPos(
                user.getX() - rightX * distance,
                user.getY(),
                user.getZ() - rightZ * distance
        );

        // Сохраняем призывателя
        leftSkeleton.addCommandTag("dark_staff_minion");

        // Ставим цель
        setTargetForSkeleton(leftSkeleton, user);

        world.spawnEntity(leftSkeleton);


        // =========================
        // СКЕЛЕТ СПРАВА
        // =========================

        WitherSkeletonEntity rightSkeleton =
                new WitherSkeletonEntity(
                        net.minecraft.entity.EntityType.WITHER_SKELETON,
                        world
                );

        rightSkeleton.setPos(
                user.getX() + rightX * distance,
                user.getY(),
                user.getZ() + rightZ * distance
        );

        // Сохраняем призывателя
        rightSkeleton.addCommandTag("dark_staff_minion");

        // Ставим цель
        setTargetForSkeleton(rightSkeleton, user);

        world.spawnEntity(rightSkeleton);
    }

    private void setTargetForSkeleton(
            WitherSkeletonEntity skeleton,
            PlayerEntity summoner
    ) {

        double radius = 20.0;

        Entity closestTarget = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : skeleton.getWorld().getOtherEntities(
                summoner,
                skeleton.getBoundingBox().expand(radius)
        )) {

            // Не атакуем самого призывателя
            if (entity == summoner) {
                continue;
            }

            // Не атакуем самого скелета
            if (entity == skeleton) {
                continue;
            }

            // Проверяем, что это живая сущность
            if (!(entity instanceof net.minecraft.entity.LivingEntity livingEntity)) {
                continue;
            }

            if (!livingEntity.isAlive()) {
                continue;
            }

            // Не атакуем других скелетов DarkStaff
            if (entity instanceof WitherSkeletonEntity) {
                if (entity.getCommandTags().contains("dark_staff_minion")) {
                    continue;
                }
            }

            double distance = skeleton.squaredDistanceTo(entity);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestTarget = entity;
            }
        }

        // Если нашли цель — устанавливаем её
        if (closestTarget instanceof net.minecraft.entity.LivingEntity livingTarget) {
            skeleton.setTarget(livingTarget);
        }
    }
}