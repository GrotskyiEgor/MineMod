package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.item.ModItem;
import net.minecraft.entity.EquipmentSlot;
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

        // Проверяем броню раз в секунду
        if (player.age % 20 != 0) {
            return;
        }

        // FIRE
        if (hasFullSet(
                player,
                ModItem.FIRE_HELMET,
                ModItem.FIRE_CHESTPLATE,
                ModItem.FIRE_LEGGINGS,
                ModItem.FIRE_BOOTS
        )) {
            System.out.println("FIRE ARMOR");
        }

        // AQUA
        else if (hasFullSet(
                player,
                ModItem.AQUA_HELMET,
                ModItem.AQUA_CHESTPLATE,
                ModItem.AQUA_LEGGINGS,
                ModItem.AQUA_BOOTS
        )) {
            System.out.println("AQUA ARMOR");
        }

        // WEED
        else if (hasFullSet(
                player,
                ModItem.WEED_HELMET,
                ModItem.WEED_CHESTPLATE,
                ModItem.WEED_LEGGINGS,
                ModItem.WEED_BOOTS
        )) {
            System.out.println("WEED ARMOR");
        }

        // DARK
        else if (hasFullSet(
                player,
                ModItem.DARK_HELMET,
                ModItem.DARK_CHESTPLATE,
                ModItem.DARK_LEGGINGS,
                ModItem.DARK_BOOTS
        )) {
            System.out.println("DARK ARMOR");
        }

        // LIGHT
        else if (hasFullSet(
                player,
                ModItem.LIGHT_HELMET,
                ModItem.LIGHT_CHESTPLATE,
                ModItem.LIGHT_LEGGINGS,
                ModItem.LIGHT_BOOTS
        )) {
            System.out.println("LIGHT ARMOR");
        }

        // EARTH
        else if (hasFullSet(
                player,
                ModItem.EARTH_HELMET,
                ModItem.EARTH_CHESTPLATE,
                ModItem.EARTH_LEGGINGS,
                ModItem.EARTH_BOOTS
        )) {
            System.out.println("EARTH ARMOR");
        }

        // HEROIC
        else if (hasFullSet(
                player,
                ModItem.HEROIC_HELMET,
                ModItem.HEROIC_CHESTPLATE,
                ModItem.HEROIC_LEGGINGS,
                ModItem.HEROIC_BOOTS
        )) {
            System.out.println("HEROIC ARMOR");
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