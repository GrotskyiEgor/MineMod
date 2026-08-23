package com.therootsofancientmagic.network;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.robe.ElementalRobeItem;
import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
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
import com.therootsofancientmagic.item.ModItem;

public class RobeAbilityServerHandler {
    public static final Identifier ROBE_ABILITY_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "robe_ability");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ROBE_ABILITY_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {

                if (player instanceof CustomArmorHolder holder) {
                    holder.ActivateAquaRobeAbility();
                    holder.ActivateFireRobeAbility();
                }

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

                        if (PlayerMana.consumeMana((IEntityDataSaver) player,10,player)) {
                            
                            if (equippedRobeStack.isOf(ModItem.AQUA_ROBE)) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 600, 0, true, false, true));
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0, true, false, true));

                            ServerWorld world = player.getServerWorld();

                            world.spawnParticles(ParticleTypes.BUBBLE, player.getX(), player.getY() + 1.0, player.getZ(), 30, 0.5, 0.5, 0.5, 0.15);
                            world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.PLAYERS, 1.0F, 1.0F);

                            player.getItemCooldownManager().set(robeItem, 700);
                        } else if (equippedRobeStack.isOf(ModItem.WEED_ROBE)) {

                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 2, true, false, true));
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 140, 0, true, false, true));

                            ServerWorld world = player.getServerWorld();
                            world.spawnParticles(ParticleTypes.END_ROD,
                                player.getX(), player.getY() + 1.0, player.getZ(), 30, 0.5, 0.5, 0.5, 0.15);

                            world.playSound(null, player.getBlockPos(),
                                    SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.2F);

                            player.getItemCooldownManager().set(robeItem, 100);

                        } else if (equippedRobeStack.isOf(ModItem.EARTH_ROBE)) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 1, true, false, true));
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 1, true, false, true));

                            ServerWorld world = player.getServerWorld();
                            world.spawnParticles(
                                new net.minecraft.particle.BlockStateParticleEffect(ParticleTypes.BLOCK, net.minecraft.block.Blocks.DIRT.getDefaultState()),
                                player.getX(), player.getY() + 0.1, player.getZ(), 40, 0.5, 0.2, 0.5, 0.1);

                            world.playSound(null, player.getBlockPos(),
                                    SoundEvents.BLOCK_ROOTED_DIRT_BREAK, SoundCategory.PLAYERS, 1.0F, 0.8F);

                            player.getItemCooldownManager().set(robeItem, 700);

                        } else if (equippedRobeStack.isOf(ModItem.FIRE_ROBE)) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0, true, false, true));
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1, true, false, true));

                            ServerWorld world = player.getServerWorld();
                            world.spawnParticles(ParticleTypes.FLAME,
                                player.getX(), player.getY() + 0.1, player.getZ(), 50, 0.5, 0.5, 0.5, 0.1);

                            world.playSound(null, player.getBlockPos(),
                                    SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

                            player.getItemCooldownManager().set(robeItem, 700);
                        }

                        }
                        
                    }
                }
            });
        });
    }
}