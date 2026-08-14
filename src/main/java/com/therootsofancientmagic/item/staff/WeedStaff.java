package com.therootsofancientmagic.item.staff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.therootsofancientmagic.entity.WindChargeEntity;

public class WeedStaff extends Item {
    private static final int COOLDOWN_TICKS = 15;

    public WeedStaff(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Check cooldown
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            // Get player look direction
            Vec3d lookDir = user.getRotationVec(1.0F);
            
            // Create wind charge slightly in front of player
            double spawnX = user.getX() + lookDir.x * 1.5;
            double spawnY = user.getEyeY();
            double spawnZ = user.getZ() + lookDir.z * 1.5;
            
            // Create entity
            WindChargeEntity windCharge = new WindChargeEntity(world, spawnX, spawnY, spawnZ, user);
            
            // Set velocity
            windCharge.setVelocity(lookDir.multiply(0.5));
            
            // Spawn it
            world.spawnEntity(windCharge);

            // Play sound
            world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
            );
        }

        // Set cooldown
        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        return TypedActionResult.success(stack, world.isClient);
    }
}
