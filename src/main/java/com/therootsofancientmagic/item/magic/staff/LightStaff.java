package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;

import net.minecraft.entity.AreaEffectCloudEntity;
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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class LightStaff extends Item {

    private static final int MANA_COST = 10;
    private static final int COOLDOWN_TICKS = 15;
    private static final double MAX_DISTANCE = 50.0;
    private static final float CLOUD_RADIUS = 2.5F;

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

        // Только серверная сторона
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {

            // Тратим ману
            if (!PlayerMana.consumeMana(
                    (IEntityDataSaver) serverPlayer,
                    MANA_COST,
                    serverPlayer)) {

                return TypedActionResult.fail(stack);
            }

            ServerWorld serverWorld = serverPlayer.getServerWorld();

            // Запоминаем точную точку СТАРТА перед телепортом
            Vec3d startPos = serverPlayer.getPos();

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
                // Если смотрим в блок — телепортируемся прямо перед ним
                teleportPos = hit.getPos().subtract(
                        direction.x,
                        direction.y,
                        direction.z
                );
            } else {
                // Если блоков нет — телепортируемся на полную дальность
                teleportPos = end;
            }

            // 1. Спавним ЖЕЛТОЕ облако дыхания дракона на месте СТАРТА
            spawnLightBreathCloud(serverWorld, startPos, serverPlayer);
            
            // Звук исчезновения на старте
            serverWorld.playSound(
                    null,
                    startPos.x, startPos.y, startPos.z,
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );

            // 2. Сама телепортация
            serverPlayer.teleport(
                    serverWorld,
                    teleportPos.x,
                    teleportPos.y,
                    teleportPos.z,
                    serverPlayer.getYaw(),
                    serverPlayer.getPitch()
            );

            // 3. Спавним ЖЕЛТОЕ облако дыхания дракона на месте ФИНИША
            spawnLightBreathCloud(serverWorld, teleportPos, serverPlayer);
            
            // Дополнительные светящиеся искры вспышки света в точке прибытия
            serverWorld.spawnParticles(
                    ParticleTypes.INSTANT_EFFECT,
                    teleportPos.x,
                    teleportPos.y + 1.0,
                    teleportPos.z,
                    50,
                    0.5, 0.5, 0.5,
                    0.1
            );

            // Звук прибытия (исправленный аметистовый звон)
            serverWorld.playSound(
                    null,
                    teleportPos.x, teleportPos.y, teleportPos.z,
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    SoundCategory.PLAYERS,
                    1.2F,
                    1.3F
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

    /**
     * Создает желтое магическое облако дыхания дракона.
     */
    private void spawnLightBreathCloud(ServerWorld world, Vec3d pos, PlayerEntity user) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, pos.x, pos.y, pos.z);
        
        cloud.setOwner(user);
        cloud.setParticleType(ParticleTypes.ENTITY_EFFECT); // Окрашиваемый тип частиц
        cloud.setRadius(CLOUD_RADIUS);
        cloud.setRadiusOnUse(-0.1F);
        cloud.setWaitTime(0);
        cloud.setDuration(80); // Облако висит 4 секунды
        cloud.setRadiusGrowth(-0.02F);
        
        // Цвет: Ярко-желтый золотистый (0xFFD700)
        cloud.setColor(0xFFD700); 
        
        // Накладывает эффект Свечения (Glowing) на всех, кто попал в облако
        cloud.addEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0));

        world.spawnEntity(cloud);
    }
}
