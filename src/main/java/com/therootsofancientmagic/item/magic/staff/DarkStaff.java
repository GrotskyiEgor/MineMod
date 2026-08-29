package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

    private static final int COOLDOWN_TICKS = 400;
    private static final double WAVE_RADIUS = 5.0;
    private static final double KNOCKBACK_STRENGTH = 10.0;
    private static final int EFFECT_DURATION = 400;

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

            knockbackNearbyEntities(world, user);

            ServerWorld serverWorld = (ServerWorld) world;

            // МНОГО ФИОЛЕТОВЫХ ЧАСТИЦ
            spawnDarkParticles(serverWorld, user.getPos());

            // НЕВИДИМОСТЬ
            user.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.INVISIBILITY,
                    EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));

            // СКОРОСТЬ
            user.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    EFFECT_DURATION,
                    1,
                    false,
                    false,
                    true
            ));

            // СПАВН ОБЛАКА ДЫХАНИЯ ДРАКОНА
            spawnDragonBreathCloud(serverWorld, user);

            playSoundBarrage(world, user);

            user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    /**
     * Создает спавнящееся облако Дыхания Дракона, наносящее урон.
     */
    private void spawnDragonBreathCloud(ServerWorld world, PlayerEntity user) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, user.getX(), user.getY(), user.getZ());
        
        cloud.setOwner(user);
        cloud.setParticleType(ParticleTypes.DRAGON_BREATH);
        cloud.setRadius((float) WAVE_RADIUS);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setWaitTime(0);
        cloud.setDuration(100); // Облако висит 5 секунд (100 тиков)
        cloud.setRadiusGrowth(-0.05F); // Постепенно сужается
        
        // Накладывает Иссушение II на врагов внутри облака
        cloud.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1)); 

        world.spawnEntity(cloud);
    }

    /**
     * Много фиолетовых частиц вокруг игрока.
     */
    private void spawnDarkParticles(ServerWorld world, Vec3d center) {

        // Фиолетовые частицы WITCH
        world.spawnParticles(
                ParticleTypes.WITCH,
                center.x,
                center.y + 1.0,
                center.z,
                300,
                2.5,
                1.5,
                2.5,
                0.15
        );

        // Фиолетовые частицы портала
        world.spawnParticles(
                ParticleTypes.PORTAL,
                center.x,
                center.y + 1.0,
                center.z,
                300,
                3.0,
                2.0,
                3.0,
                0.5
        );

        // Тёмное фиолетовое облако
        world.spawnParticles(
                ParticleTypes.DRAGON_BREATH,
                center.x,
                center.y + 1.0,
                center.z,
                200,
                2.5,
                1.5,
                2.5,
                0.08
        );

        // Ещё фиолетовые частицы вокруг ног
        world.spawnParticles(
                ParticleTypes.PORTAL,
                center.x,
                center.y + 0.2,
                center.z,
                150,
                2.5,
                0.2,
                2.5,
                0.3
        );

        // Столб частиц вверх
        for (int i = 0; i < 50; i++) {

            double x = center.x + (RANDOM.nextDouble() - 0.5) * 2.0;
            double y = center.y + RANDOM.nextDouble() * 3.0;
            double z = center.z + (RANDOM.nextDouble() - 0.5) * 2.0;

            world.spawnParticles(
                    ParticleTypes.WITCH,
                    x,
                    y,
                    z,
                    2,
                    0.1,
                    0.1,
                    0.1,
                    0.05
            );
        }
    }

    /**
     * Отбрасывает существ вокруг игрока.
     */
    private void knockbackNearbyEntities(World world, PlayerEntity user) {

        Box box = user.getBoundingBox().expand(WAVE_RADIUS);

        List<LivingEntity> entities =
                world.getEntitiesByClass(
                        LivingEntity.class,
                        box,
                        e -> e != user
                );

        for (LivingEntity entity : entities) {

            double dx = entity.getX() - user.getX();
            double dz = entity.getZ() - user.getZ();

            double distance =
                    Math.max(Math.sqrt(dx * dx + dz * dz), 0.1);

            dx /= distance;
            dz /= distance;

            entity.takeKnockback(
                    KNOCKBACK_STRENGTH,
                    -dx,
                    -dz
            );

            Vec3d velocity = entity.getVelocity();

            entity.setVelocity(
                    velocity.x,
                    0.5,
                    velocity.z
                );

            entity.velocityModified = true;
        }
    }

    /**
     * Звуки.
     */
    private void playSoundBarrage(World world, PlayerEntity user) {

        var pos = user.getBlockPos();

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_WITHER_SHOOT,
                SoundCategory.PLAYERS,
                1.0F,
                0.6F
        );

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                0.8F,
                0.5F
        );

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.PLAYERS,
                0.7F,
                1.6F
        );

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                SoundCategory.PLAYERS,
                0.4F,
                1.8F
        );

        world.playSound(
                null,
                pos,
                SoundEvents.PARTICLE_SOUL_ESCAPE,
                SoundCategory.PLAYERS,
                0.6F,
                0.5F
        );

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,
                SoundCategory.PLAYERS,
                0.5F,
                1.4F
        );
    }
}
