package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class FireStaff extends Item {
    private static final int COOLDOWN_TICKS = 15;

    private static final Random RANDOM = new Random();

    public FireStaff(Settings settings) {
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
            // Принудительно кастим игрока к серверному типу для работи с пакетами мани
            if (user instanceof ServerPlayerEntity serverPlayer) {
                
                // ВЫЗЫВАЕМ СПИСАНИЕ МАНЫ: Тратим ровно 10 единиц (1 кружочек на худ-баре)
                if (PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
                    
                    // МАНЫ ХВАТИЛО: Спавним огненний шар и запускаем звук
                    castSpell(world, user);

                    world.playSound(
                            null,
                            user.getBlockPos(),
                            SoundEvents.ENTITY_BLAZE_SHOOT,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.0F
                    );
                    world.playSound(
                            null,
                            user.getBlockPos(),
                            SoundEvents.ITEM_FIRECHARGE_USE,
                            SoundCategory.PLAYERS,
                            1.2F,
                            0.8F
                    );
                    world.playSound(
                            null,
                            user.getBlockPos(),
                            SoundEvents.ENTITY_GHAST_SHOOT,
                            SoundCategory.PLAYERS,
                            0.6F,
                            1.5F
                    );

                    // Устанавливаем кулдаун ТОЛЬКО если посох успешно вистрелил заклинанием
                    user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

                } else {
                    // МАНЫ НЕ ХВАТИЛО: Заклинание полностью блокируется, посох выдает осечку
                    return TypedActionResult.fail(stack);
                }
            }
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    private void castSpell(World world, PlayerEntity user) {
        // Направление взгляда игрока
        var lookVec = user.getRotationVec(1.0F);

        FireballEntity fireball = new FireballEntity(
                world,
                user,
                lookVec.x * 0.3,
                lookVec.y * 0.3,
                lookVec.z * 0.3,
                3 // сила взрыва
        );

        Vec3d spawnPos = new Vec3d(
                user.getX() + lookVec.x,
                user.getEyeY() - 0.1,
                user.getZ() + lookVec.z
        );

        fireball.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        world.spawnEntity(fireball);

        spawnMuzzleEffects(world, spawnPos, lookVec);
    }

    /** Огненный "хлопок" частиц и дымный шлейф в точке вылета шара. */
    private void spawnMuzzleEffects(World world, Vec3d origin, Vec3d direction) {
        sphereBurst(world, origin, 40, ParticleTypes.FLAME, 0.35);
        sphereBurst(world, origin, 15, ParticleTypes.LAVA, 0.2);
        sphereBurst(world, origin, 25, ParticleTypes.LARGE_SMOKE, 0.15);

        for (int i = 1; i <= 8; i++) {
            Vec3d point = origin.add(direction.multiply(i * 0.4));
            world.addParticle(ParticleTypes.SMOKE, point.x, point.y, point.z, 0, 0.02, 0);
        }
    }

    /** Разлёт count частиц равномерно по сфере из одной точки. */
    private void sphereBurst(World world, Vec3d origin, int count, ParticleEffect particle, double maxSpeed) {
        for (int i = 0; i < count; i++) {
            double u = RANDOM.nextDouble() * 2.0 - 1.0;
            double theta = RANDOM.nextDouble() * Math.PI * 2.0;
            double sqrtTerm = Math.sqrt(1.0 - u * u);

            double dirX = sqrtTerm * Math.cos(theta);
            double dirY = u;
            double dirZ = sqrtTerm * Math.sin(theta);
            double speed = RANDOM.nextDouble() * maxSpeed;

            world.addParticle(particle, origin.x, origin.y, origin.z, dirX * speed, dirY * speed, dirZ * speed);
        }
    }
}