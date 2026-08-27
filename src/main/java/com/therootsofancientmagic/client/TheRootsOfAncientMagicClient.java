package com.therootsofancientmagic.client;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import com.therootsofancientmagic.client.biomes.earth.EarthBiomeParticles;
import com.therootsofancientmagic.client.biomes.earth.EarthWindOverlay;
import com.therootsofancientmagic.entity.ModEntities;
import com.therootsofancientmagic.item.tools.unique_tools.BattleAxeItem;
import net.minecraft.client.option.Perspective;
import com.therootsofancientmagic.network.ModMessages;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class TheRootsOfAncientMagicClient implements ClientModInitializer {
    public static final Identifier ROBE_ABILITY_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "robe_ability");
    public static final Identifier FIRST_RING_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "first_ring_ability");
    public static final Identifier SECOND_RING_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "second_ring_ability");
    public static KeyBinding robeAbilityKey;
    public static KeyBinding firstRingKey;
    public static KeyBinding secondRingKey;

    private static boolean wasUsingBattleAxe = false;

    @Override
    public void onInitializeClient() {
        EarthBiomeParticles.register();
        EarthWindOverlay.register();

        // EntityRendererRegistry.register(ModEntities.WIND_CHARGE, FlyingItemEntityRenderer::new);

        robeAbilityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.therootsofancientmagic.robe_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.therootsofancientmagic.general"
        ));

        firstRingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.therootsofancientmagic.first_ring_ability",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.therootsofancientmagic.keys"
        ));

        secondRingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.therootsofancientmagic.second_ring_ability",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.therootsofancientmagic.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                
                // Проверяет, держит ли игрок чтото и если етот предмет боевой топор
                boolean isCurrentlyUsingBattleAxe = client.player.isUsingItem() && 
                        client.player.getActiveItem().getItem() instanceof BattleAxeItem;

                if (isCurrentlyUsingBattleAxe && !wasUsingBattleAxe) {
                    // Если абилку нажали то переводит игрока в 3 лицо
                    if (client.options.getPerspective() == Perspective.FIRST_PERSON) {
                        client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                    }
                    wasUsingBattleAxe = true;
                } else if (!isCurrentlyUsingBattleAxe && wasUsingBattleAxe) {
                    // Если абилка завершилась то переводит игрока в 1 лицо
                    if (client.options.getPerspective() == Perspective.THIRD_PERSON_BACK) {
                        client.options.setPerspective(Perspective.FIRST_PERSON);
                    }
                    wasUsingBattleAxe = false;
                }

                while (robeAbilityKey.wasPressed()) {
                    ClientPlayNetworking.send(ROBE_ABILITY_PACKET, PacketByteBufs.create());
                }

                while (firstRingKey.wasPressed()) {
                    ClientPlayNetworking.send(FIRST_RING_PACKET, PacketByteBufs.create());
                }

                while (secondRingKey.wasPressed()) {
                    ClientPlayNetworking.send(SECOND_RING_PACKET, PacketByteBufs.create());
                }
            }
        });
        
        ModMessages.registerS2CPackets();

        HudRenderCallback.EVENT.register(new ManaHudOverlay());

    }
}
