package com.therootsofancientmagic.network;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.robe.ElementalRobeItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class RobeAbilityServerHandler {
    public static final Identifier ROBE_ABILITY_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "robe_ability");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ROBE_ABILITY_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ItemStack equippedRobeStack = ItemStack.EMPTY;

                if (player instanceof CustomArmorHolder holder) {
                    Inventory inventory = holder.getCustomArmorInventory();
                    for (int i = 0; i < 4; i++) {
                        ItemStack stack = inventory.getStack(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof ElementalRobeItem) {
                            equippedRobeStack = stack;
                            break;
                        }
                    }
                }

                if (!equippedRobeStack.isEmpty()) {
                    Item robeItem = equippedRobeStack.getItem();

                    if (!player.getItemCooldownManager().isCoolingDown(robeItem)) {

                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 2, true, false, true));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 140, 0, true, false, true));

                        ServerWorld world = player.getServerWorld();
                        world.spawnParticles(ParticleTypes.END_ROD,
                                player.getX(), player.getY() + 1.0, player.getZ(),
                                30, 0.5, 0.5, 0.5, 0.15);

                        world.playSound(null, player.getBlockPos(),
                                SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.2F);

                        player.getItemCooldownManager().set(robeItem, 100);
                    }
                }
            });
        });
    }
}