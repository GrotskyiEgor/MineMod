package com.therootsofancientmagic.item.staff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireStaff extends Item {
    private static final int COOLDOWN_TICKS = 40;

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
            castSpell(world, user);

            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_BLAZE_SHOOT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        return TypedActionResult.success(stack, world.isClient);
    }

    private void castSpell(World world, PlayerEntity user) {
        // Направление взгляда игрока
        var lookVec = user.getRotationVec(1.0F);

        FireballEntity fireball = new FireballEntity(
                world,
                user,
                lookVec.x * 0.1,
                lookVec.y * 0.1,
                lookVec.z * 0.1,
                1 // сила взрыва
        );

        fireball.setPos(
                user.getX() + lookVec.x,
                user.getEyeY() - 0.1,
                user.getZ() + lookVec.z
        );

        world.spawnEntity(fireball);
    }
}