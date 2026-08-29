package com.therootsofancientmagic.item.magic.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext;
import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class AirStaff extends Item {

    // Радиус, в котором притягиваются сущности
    private static final double PULL_RADIUS = 3.0D;
    // Дальность луча (как далеко смотрит игрок)
    private static final double RAY_RANGE = 20.0D;
    // Скорость подброса вверх
    private static final double LAUNCH_VELOCITY_Y = 1.8D;
    // Сила притяжения к центральной точке
    private static final double PULL_STRENGTH = 0.6D;

    public AirStaff(Settings settings) {
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
            if (user instanceof ServerPlayerEntity serverPlayer) {
                
                if (PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
                    
                    Vec3d targetPoint = getTargetPoint(world, user);

                    Box searchBox = new Box(
                            targetPoint.x - PULL_RADIUS, targetPoint.y - PULL_RADIUS, targetPoint.z - PULL_RADIUS,
                            targetPoint.x + PULL_RADIUS, targetPoint.y + PULL_RADIUS, targetPoint.z + PULL_RADIUS
                    );

                        List<Entity> entities = world.getOtherEntities(user, searchBox,
                            entity -> entity.squaredDistanceTo(targetPoint) > 0.0001D);

                        for (Entity entity : entities) {
                        Vec3d toCenter = targetPoint.subtract(entity.getPos());
                        Vec3d pullVec = toCenter.normalize().multiply(PULL_STRENGTH);

                        Vec3d newVelocity = entity.getVelocity()
                            .add(pullVec.x, pullVec.y, pullVec.z)
                            .add(0, LAUNCH_VELOCITY_Y, 0);

                        entity.setVelocity(newVelocity);
                        entity.velocityModified = true;
                        entity.fallDistance = 0.0F;
                        }

                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
                            SoundCategory.PLAYERS, 1.0F, 1.0F);
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PHANTOM_FLAP,
                            SoundCategory.PLAYERS, 1.2F, 0.6F);
                    world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_TRIDENT_RIPTIDE_1,
                            SoundCategory.PLAYERS, 1.0F, 1.4F);

                    if (world instanceof ServerWorld serverWorld) {
                        spawnVortexParticles(serverWorld, targetPoint);
                        spawnGustBurst(serverWorld, targetPoint);

                        serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                                targetPoint.x, targetPoint.y, targetPoint.z,
                                1, 0.2, 0.2, 0.2, 0.0);
                    }

                    user.getItemCooldownManager().set(this, 40);

                } else {

                    return TypedActionResult.fail(stack);
                }
            }
        }
         return TypedActionResult.success(stack, world.isClient);
    }

    /** Вихрь из частиц, закручивающийся к центральной точке — визуализация притяжения. */
    private void spawnVortexParticles(ServerWorld world, Vec3d center) {
        int arms = 30;
        for (int i = 0; i < arms; i++) {
            double angle = (Math.PI * 2.0 * i) / arms;
            double radius = PULL_RADIUS;

            double x = center.x + Math.cos(angle) * radius;
            double z = center.z + Math.sin(angle) * radius;
            double y = center.y + (Math.random() - 0.5) * PULL_RADIUS;

            Vec3d toCenter = center.subtract(x, y, z).normalize().multiply(0.15);
            world.spawnParticles(ParticleTypes.CLOUD, x, y, z, 0, toCenter.x, toCenter.y, toCenter.z, 0.02);
            world.spawnParticles(ParticleTypes.END_ROD, x, y, z, 0, toCenter.x, toCenter.y, toCenter.z, 0.01);
        }
    }

    /** Всплеск воздушных частиц вверх в момент подброса сущностей. */
    private void spawnGustBurst(ServerWorld world, Vec3d center) {
        world.spawnParticles(ParticleTypes.CLOUD, center.x, center.y, center.z, 25, 0.6, 0.2, 0.6, 0.08);
        world.spawnParticles(ParticleTypes.SWEEP_ATTACK, center.x, center.y + 0.3, center.z, 3, 0.3, 0.1, 0.3, 0.0);
        world.spawnParticles(ParticleTypes.CRIT, center.x, center.y, center.z, 20, 0.8, 0.4, 0.8, 0.15);
    }

    private Vec3d getTargetPoint(World world, PlayerEntity player) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d end = start.add(look.multiply(RAY_RANGE));

        BlockHitResult hitResult = world.raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hitResult.getPos();
        }

        return end;
    }
}