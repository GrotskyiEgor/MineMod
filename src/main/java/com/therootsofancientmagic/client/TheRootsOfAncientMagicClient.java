package com.therootsofancientmagic.client;

import com.therootsofancientmagic.TheRootsOfAncientMagic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class TheRootsOfAncientMagicClient implements ClientModInitializer {
    public static final Identifier ROBE_ABILITY_PACKET = new Identifier(TheRootsOfAncientMagic.MOD_ID, "robe_ability");
    public static KeyBinding robeAbilityKey;

    @Override
    public void onInitializeClient() {
        robeAbilityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.therootsofancientmagic.robe_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.therootsofancientmagic.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (robeAbilityKey.wasPressed()) {
                if (client.player != null) {
                    ClientPlayNetworking.send(ROBE_ABILITY_PACKET, PacketByteBufs.create());
                }
            }
        });
        
        com.therootsofancientmagic.network.ModMessages.registerS2CPackets();

        net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register(new ManaHudOverlay());
    }
}