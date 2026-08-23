package com.therootsofancientmagic.network;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.ModItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class RobeAbilityServerHandler {
    public static final Identifier ROBE_ABILITY_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "robe_ability");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ROBE_ABILITY_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player instanceof CustomArmorHolder holder) {
                    Inventory inventory = holder.getCustomArmorInventory();
                    if (inventory != null) {
                        ItemStack backStack = inventory.getStack(0);

                        if (!backStack.isEmpty() && backStack.isOf(ModItem.FIRE_ROBE)) {
                            if (!player.getItemCooldownManager().isCoolingDown(ModItem.FIRE_ROBE)) {
                                
                                holder.activateFireRobeAbility();

                                player.getItemCooldownManager().set(ModItem.FIRE_ROBE, 240);

                                player.getServerWorld().playSound(
                                    null, 
                                    player.getBlockPos(),
                                    SoundEvents.ITEM_FIRECHARGE_USE, 
                                    SoundCategory.PLAYERS, 
                                    1.0F, 
                                    1.0F
                                );
                            }
                        }
                    }
                }
            });
        });
    }
}