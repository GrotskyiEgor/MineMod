package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana; // Импортируем нашу систему мани
import com.therootsofancientmagic.util.IEntityDataSaver; // Импортируем кармашек NBT данних
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity; // Добавили серверного игрока для пакетов мани
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireStaff extends Item {
    private static final int COOLDOWN_TICKS = 15;

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

        fireball.setPos(
                user.getX() + lookVec.x,
                user.getEyeY() - 0.1,
                user.getZ() + lookVec.z
        );

        world.spawnEntity(fireball);
    }
}
