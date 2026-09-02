package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.item.ModItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkPlayerArmor(CallbackInfo ci) {

        PlayerEntity player = (PlayerEntity) (Object) this;

        // Проверяем броню один раз в секунду
        if (player.age % 20 != 0) {
            return;
        }

        if (hasFullSet(
                player,
                ModItem.FIRE_HELMET,
                ModItem.FIRE_CHESTPLATE,
                ModItem.FIRE_LEGGINGS,
                ModItem.FIRE_BOOTS
        )) {

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.FIRE_RESISTANCE,
                    40,
                    0,
                    false,
                    true,
                    true
            ));
        }

        else if (hasFullSet(
                player,
                ModItem.AQUA_HELMET,
                ModItem.AQUA_CHESTPLATE,
                ModItem.AQUA_LEGGINGS,
                ModItem.AQUA_BOOTS
        )) {

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.DOLPHINS_GRACE,
                    40,
                    0,
                    false,
                    true,
                    true
            ));
        }

        else if (hasFullSet(
                player,
                ModItem.AIR_HELMET,
                ModItem.AIR_CHESTPLATE,
                ModItem.AIR_LEGGINGS,
                ModItem.AIR_BOOTS
        )) {

            // Скорость I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    40,
                    0,
                    false,
                    true,
                    true
            ));

            // Прыгучесть I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.JUMP_BOOST,
                    40,
                    0,
                    false,
                    true,
                    true
            ));
        }

        else if (hasFullSet(
                player,
                ModItem.DARK_HELMET,
                ModItem.DARK_CHESTPLATE,
                ModItem.DARK_LEGGINGS,
                ModItem.DARK_BOOTS
        )) {

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NIGHT_VISION,
                    40,
                    0,
                    false,
                    true,
                    true
            ));
        }

        else if (hasFullSet(
                player,
                ModItem.LIGHT_HELMET,
                ModItem.LIGHT_CHESTPLATE,
                ModItem.LIGHT_LEGGINGS,
                ModItem.LIGHT_BOOTS
        )) {

            // Регенерация II
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.REGENERATION,
                    40,
                    1,
                    false,
                    true,
                    true
            ));
        }

        else if (hasFullSet(
                player,
                ModItem.EARTH_HELMET,
                ModItem.EARTH_CHESTPLATE,
                ModItem.EARTH_LEGGINGS,
                ModItem.EARTH_BOOTS
        )) {

            // Спешка I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HASTE,
                    40,
                    0,
                    false,
                    true,
                    true
            ));
        }

        else if (hasFullSet(
                player,
                ModItem.HEROIC_HELMET,
                ModItem.HEROIC_CHESTPLATE,
                ModItem.HEROIC_LEGGINGS,
                ModItem.HEROIC_BOOTS
        )) {

            // Скорость I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    40,
                    0,
                    false,
                    true,
                    true
            ));

            // Сила I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.STRENGTH,
                    40,
                    0,
                    false,
                    true,
                    true
            ));

            // Спешка I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HASTE,
                    40,
                    0,
                    false,
                    true,
                    true
            ));

            // Ночное зрение
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NIGHT_VISION,
                    40,
                    0,
                    false,
                    true,
                    true
            ));

            // Регенерация I
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.REGENERATION,
                    40,
                    0,
                    false,
                    true,
                    true
            ));
        }
    }

    private boolean hasFullSet(
            PlayerEntity player,
            Item helmet,
            Item chestplate,
            Item leggings,
            Item boots
    ) {

        return player.getEquippedStack(EquipmentSlot.HEAD).isOf(helmet)
                && player.getEquippedStack(EquipmentSlot.CHEST).isOf(chestplate)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(leggings)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(boots);
    }      
}