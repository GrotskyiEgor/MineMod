package com.therootsofancientmagic.item.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class WindChargeEntity extends Entity {
    private int lifetime = 0;
    private static final int MAX_LIFETIME = 100;
    private static final double PULL_RADIUS = 3.0;
    private static final double PULL_STRENGTH = 0.8;
    private Vec3d targetDirection = Vec3d.ZERO;
    private Entity owner;

    public WindChargeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public WindChargeEntity(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Vec3d targetDir) {
        super(com.therootsofancientmagic.entity.ModEntities.WIND_CHARGE, world);
        this.setPos(x, y, z);
        this.setVelocity(velocityX, velocityY, velocityZ);
        this.targetDirection = targetDir;
    }

    @Override
    public void tick() {
        super.tick();
        lifetime++;

        // Если время жизни истекло - притягиваем существа
        if (lifetime >= MAX_LIFETIME) {
            performPullEffect();
            this.discard();
            return;
        }

        // Движение
        Vec3d pos = this.getPos().add(this.getVelocity());
        this.setPos(pos.x, pos.y, pos.z);

        // Замедляем снаряд
        this.setVelocity(this.getVelocity().multiply(0.95));
    }

    private void performPullEffect() {
        Vec3d center = this.getPos();
        Box pullArea = new Box(
                center.x - PULL_RADIUS, center.y - PULL_RADIUS, center.z - PULL_RADIUS,
                center.x + PULL_RADIUS, center.y + PULL_RADIUS, center.z + PULL_RADIUS
        );

        List<Entity> entities = this.getWorld().getOtherEntities(owner, pullArea);

        // Притягиваем существ в направлении, куда смотрел игрок
        for (Entity entity : entities) {
            if (entity == owner) continue;
            
            Vec3d pullVector = targetDirection.normalize();
            entity.addVelocity(
                    pullVector.x * PULL_STRENGTH,
                    pullVector.y * PULL_STRENGTH,
                    pullVector.z * PULL_STRENGTH
            );
            entity.velocityModified = true;
        }
    }

    public void setOwner(Entity ownerEntity) {
        this.owner = ownerEntity;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void initDataTracker() {
        // No data to track for this simple entity
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
    }
}
