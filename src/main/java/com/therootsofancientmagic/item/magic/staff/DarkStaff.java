package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class DarkStaff extends Item {

    private static final int COOLDOWN_TICKS = 400;      // 20 сек
    private static final double WAVE_RADIUS = 5.0;       // радиус отбрасывания
    private static final double KNOCKBACK_STRENGTH = 2.5;
    private static final int EFFECT_DURATION = 400;      // 20 сек
    private static final int BLACK_SPAM_COUNT = 600;     // сколько чёрных частиц спамим при касте
    private static final int DECOY_PARTICLE_COUNT = 250; // частиц-обманок на месте каста
    private static final double DECOY_RADIUS = 1.8;

    private static final Random RANDOM = new Random();

    public DarkStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            if (!PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 6, serverPlayer)) {
                return TypedActionResult.fail(stack);
            }

            Vec3d castPos = user.getPos();

            castDarkWave(world, user);
            spawnBlackWaveSpam(world, castPos);

            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, EFFECT_DURATION, 0, false, false, true));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, EFFECT_DURATION, 1, false, false, true));

            spawnDecoyBurst(world, castPos);

            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.PLAYERS, 1.0F, 0.6F);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.8F, 0.5F);

            user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    /** Отбрасывает существ в радиусе WAVE_RADIUS вокруг игрока. */
    private void castDarkWave(World world, PlayerEntity user) {
        Box box = user.getBoundingBox().expand(WAVE_RADIUS);
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, e -> e != user);

        for (LivingEntity entity : entities) {
            double dx = entity.getX() - user.getX();
            double dz = entity.getZ() - user.getZ();
            double distance = Math.max(Math.sqrt(dx * dx + dz * dz), 0.1);
            dx /= distance;
            dz /= distance;

            entity.takeKnockback(KNOCKBACK_STRENGTH, -dx, -dz);

            Vec3d velocity = entity.getVelocity();
            entity.setVelocity(velocity.x, 0.5, velocity.z);
            entity.velocityModified = true;
        }
    }

    /**
     * Взрыв в стиле фейерверка, только чёрный: из одной точки
     * над игроком чёрные искры разлетаются равномерно во все
     * стороны сферой (а не просто по кругу вокруг ног).
     */
    private void spawnBlackWaveSpam(World world, Vec3d center) {
        Vec3d origin = center.add(0, 1.2, 0);

        for (int i = 0; i < BLACK_SPAM_COUNT; i++) {

            // случайная точка на сфере (равномерное распределение)
            double u = RANDOM.nextDouble() * 2.0 - 1.0;
            double theta = RANDOM.nextDouble() * Math.PI * 2.0;
            double sqrtTerm = Math.sqrt(1.0 - u * u);

            double dirX = sqrtTerm * Math.cos(theta);
            double dirY = u;
            double dirZ = sqrtTerm * Math.sin(theta);

            double speed = 0.15 + RANDOM.nextDouble() * 0.25;

            world.addParticle(
                    ParticleTypes.SQUID_INK,
                    origin.x,
                    origin.y,
                    origin.z,
                    dirX * speed,
                    dirY * speed,
                    dirZ * speed
            );
        }
    }

    /**
     * Плотное белое облако-обманка на месте каста.
     * Игрок уходит в невидимость, а враги видят яркую вспышку
     * там, где он был.
     */
    private void spawnDecoyBurst(World world, Vec3d castPos) {
        spawnSphereBurst(world, castPos, DECOY_RADIUS, DECOY_PARTICLE_COUNT, ParticleTypes.END_ROD, 1.0);
        spawnSphereBurst(world, castPos, DECOY_RADIUS, DECOY_PARTICLE_COUNT / 2, ParticleTypes.CLOUD, 0.5);
        spawnRing(world, castPos.add(0, 0.1, 0), DECOY_RADIUS * 0.6, 60, ParticleTypes.END_ROD, 0.08);
    }

    /** Рисует кольцо частиц радиусом radius вокруг center. */
    private void spawnRing(World world, Vec3d center, double radius, int count, ParticleEffect particle, double velY) {
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2.0 * i) / count;
            double x = center.x + Math.cos(angle) * radius;
            double z = center.z + Math.sin(angle) * radius;
            world.addParticle(particle, x, center.y, z, 0, velY, 0);
        }
    }

    /** Разбрасывает count частиц случайно внутри сферы радиусом radius вокруг center. */
    private void spawnSphereBurst(World world, Vec3d center, double radius, int count, ParticleEffect particle, double velScale) {
        for (int i = 0; i < count; i++) {
            double x = center.x + (RANDOM.nextDouble() * 2.0 - 1.0) * radius;
            double y = center.y + RANDOM.nextDouble() * 2.2;
            double z = center.z + (RANDOM.nextDouble() * 2.0 - 1.0) * radius;

            double velX = (RANDOM.nextDouble() - 0.5) * 0.05 * velScale;
            double velY = RANDOM.nextDouble() * 0.05 * velScale;
            double velZ = (RANDOM.nextDouble() - 0.5) * 0.05 * velScale;

            world.addParticle(particle, x, y, z, velX, velY, velZ);
        }
    }
}