// package com.therootsofancientmagic.mixin;

// import com.therootsofancientmagic.item.ModItem;
// import net.minecraft.entity.EquipmentSlot;
// import net.minecraft.entity.effect.StatusEffect;
// import net.minecraft.entity.effect.StatusEffects;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.item.Item;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// @Mixin(PlayerEntity.class)
// public class PlayerTickMixin {

//     @Inject(method = "tick", at = @At("TAIL"))
//     private void checkPlayerArmor(CallbackInfo ci) {

//         PlayerEntity player = (PlayerEntity) (Object) this;

//         // Проверяем броню раз в секунду
//         if (player.age % 20 != 0) {
//             return;
//         }

//         // +2 золотых 
        
//         // FIRE
//         if (hasFullSet(
//                 player,
//                 ModItem.FIRE_HELMET,
//                 ModItem.FIRE_CHESTPLATE,
//                 ModItem.FIRE_LEGGINGS,
//                 ModItem.FIRE_BOOTS
//         )) {
//             // Фаир резист
//             addEffect(player,StatusEffects.FIRE_RESISTANCE, 1);
            
            
            
//         } 

//         // AQUA
//         else if (hasFullSet(
//                 player,
//                 ModItem.AQUA_HELMET,
//                 ModItem.AQUA_CHESTPLATE,
//                 ModItem.AQUA_LEGGINGS,
//                 ModItem.AQUA_BOOTS
//         )) {
//             // Ефект дельфина
//            addEffect(player, StatusEffects.DOLPHINS_GRACE, 1);;
//         }

//         // WEED
//         else if (hasFullSet(
//                 player,
//                 ModItem.WEED_HELMET,
//                 ModItem.WEED_CHESTPLATE,
//                 ModItem.WEED_LEGGINGS,
//                 ModItem.WEED_BOOTS
//         )) {
//             // Прыгучесть 1 + скорость 1
//             addEffect(player, StatusEffects.JUMP_BOOST, 0);
            
//             addEffect(player, StatusEffects.SPEED, 0)
//         }

//         // DARK
//         else if (hasFullSet(
//                 player,
//                 ModItem.DARK_HELMET,
//                 ModItem.DARK_CHESTPLATE,
//                 ModItem.DARK_LEGGINGS,
//                 ModItem.DARK_BOOTS
//         )) {
//             // Ночное видинье 
//             addEffect(player, StatusEffects.NIGHT_VISION, 0);
//         }

//         // LIGHT
//         else if (hasFullSet(
//                 player,
//                 ModItem.LIGHT_HELMET,
//                 ModItem.LIGHT_CHESTPLATE,
//                 ModItem.LIGHT_LEGGINGS,
//                 ModItem.LIGHT_BOOTS
//         )) {
//             // Регенерация 2
//              addEffect(player, StatusEffects.REGENERATION, 1);
//         }

//         // EARTH
//         else if (hasFullSet(
//                 player,
//                 ModItem.EARTH_HELMET,
//                 ModItem.EARTH_CHESTPLATE,
//                 ModItem.EARTH_LEGGINGS,
//                 ModItem.EARTH_BOOTS
//         )) {
//             // Спешка 1
//             addEffect(player, StatusEffects.HASTE, 0);
//         }

//         // HEROIC
//         else if (hasFullSet(
//                 player,
//                 ModItem.HEROIC_HELMET,
//                 ModItem.HEROIC_CHESTPLATE,
//                 ModItem.HEROIC_LEGGINGS,
//                 ModItem.HEROIC_BOOTS
//         )) {
//             // Скорость 1, Сила 1, Спешка 1, Ночное виденье, Регенерация 1
//             // Скорость I
//             addEffect(player, StatusEffects.SPEED, 0);

//             Сила I
//             addEffect(player, StatusEffects.STRENGTH, 0);

//             Спешка I
//             addEffect(player, StatusEffects.HASTE, 0);

//             Ночное зрение
//             addEffect(player, StatusEffects.NIGHT_VISION, 0);

//             Регенерация I
//             addEffect(player, StatusEffects.REGENERATION, 0);
//         }
//     }

//     private boolean hasFullSet(
//             PlayerEntity player,
//             Item helmet,
//             Item chestplate,
//             Item leggings,
//             Item boots
//     ) {
//         return player.getEquippedStack(EquipmentSlot.HEAD).isOf(helmet)
//                 && player.getEquippedStack(EquipmentSlot.CHEST).isOf(chestplate)
//                 && player.getEquippedStack(EquipmentSlot.LEGS).isOf(leggings)
//                 && player.getEquippedStack(EquipmentSlot.FEET).isOf(boots);
//     }
// }