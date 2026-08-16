package com.therootsofancientmagic.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WindChargeEntity extends Entity {
    private static final int MAX_LIFETIME = 80; // 4 seconds
    private static final double PULL_RADIUS = 5.0;
    private static final double PULL_STRENGTH = 0.3;
    
    private int lifetime = 0;

    public WindChargeEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true; // Pass through blocks
    }

    public WindChargeEntity(World world, double x, double y, double z, PlayerEntity owner) {
        this(ModEntities.WIND_CHARGE, world);
        this.setPosition(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        
        lifetime++;
        
        // Apply slow drag to velocity
        Vec3d vel = this.getVelocity();
        this.setVelocity(vel.multiply(0.96));
        
        // Manual movement
        double newX = this.getX() + this.getVelocity().x;
        double newY = this.getY() + this.getVelocity().y;
        double newZ = this.getZ() + this.getVelocity().z;
        this.setPosition(newX, newY, newZ);
        
        // Server-side logic only
        if (!this.getWorld().isClient && lifetime >= MAX_LIFETIME) {
            this.tryPullEntities();
            this.discard();
        }
    }

    private void tryPullEntities() {
        try {
            World world = this.getWorld();
            double posX = this.getX();
            double posY = this.getY();
            double posZ = this.getZ();
            
            // Check all players in world
            for (PlayerEntity player : world.getPlayers()) {
                if (player == null) continue;
                
                double dx = posX - player.getX();
                double dy = posY - player.getY();
                double dz = posZ - player.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                
                // Only pull if in range
                if (distance > 0 && distance < PULL_RADIUS) {
                    // Normalize direction
                    double nx = dx / distance;
                    double ny = dy / distance;
                    double nz = dz / distance;
                    
                    // Add velocity (don't replace it)
                    player.addVelocity(
                        nx * PULL_STRENGTH,
                        ny * PULL_STRENGTH * 0.5, // Less vertical pull
                        nz * PULL_STRENGTH
                    );
                }
            }
        } catch (Exception e) {
            // Ignore any errors
        }
    }

    @Override
    protected void initDataTracker() {
        // No tracked data
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.lifetime = nbt.getInt("lifetime");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("lifetime", lifetime);
    }
}
