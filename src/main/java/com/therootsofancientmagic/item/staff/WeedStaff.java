package com.therootsofancientmagic.item.staff;

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

import java.util.List;

public class WeedStaff extends Item {

    // Радиус, в котором притягиваются сущности
    private static final double PULL_RADIUS = 3.0D;
    // Дальность луча (как далеко смотрит игрок)
    private static final double RAY_RANGE = 20.0D;
    // Скорость подброса вверх
    private static final double LAUNCH_VELOCITY_Y = 1.8D;
    // Сила притяжения к центральной точке
    private static final double PULL_STRENGTH = 0.6D;

    public WeedStaff(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            Vec3d targetPoint = getTargetPoint(world, user);

            Box searchBox = new Box(
                    targetPoint.x - PULL_RADIUS, targetPoint.y - PULL_RADIUS, targetPoint.z - PULL_RADIUS,
                    targetPoint.x + PULL_RADIUS, targetPoint.y + PULL_RADIUS, targetPoint.z + PULL_RADIUS
            );

            List<Entity> entities = world.getOtherEntities(user, searchBox,
                    entity -> entity.squaredDistanceTo(targetPoint) <= PULL_RADIUS * PULL_RADIUS);

            for (Entity entity : entities) {
                Vec3d toCenter = targetPoint.subtract(entity.getPos());
                Vec3d pullVec = toCenter.lengthSquared() > 0.0001
                        ? toCenter.normalize().multiply(PULL_STRENGTH)
                        : Vec3d.ZERO;

                Vec3d newVelocity = entity.getVelocity()
                        .add(pullVec.x, pullVec.y, pullVec.z)
                        .add(0, LAUNCH_VELOCITY_Y, 0);

                entity.setVelocity(newVelocity);
                entity.velocityModified = true;
                entity.fallDistance = 0.0F;
            }

            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
                    SoundCategory.PLAYERS, 1.0F, 1.0F);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                        targetPoint.x, targetPoint.y, targetPoint.z,
                        1, 0.2, 0.2, 0.2, 0.0);
            }
        }

        user.getItemCooldownManager().set(this, 40);
        return TypedActionResult.success(stack, world.isClient);
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