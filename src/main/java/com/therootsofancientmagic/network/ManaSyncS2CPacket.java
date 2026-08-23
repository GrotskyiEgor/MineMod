package com.therootsofancientmagic.network;

import com.therootsofancientmagic.util.IEntityDataSaver;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class ManaSyncS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        
        int mana = buf.readInt();

       
        client.execute(() -> {
            if (client.player instanceof IEntityDataSaver dataSaver) {
                dataSaver.getPersistentData().putInt("mana", mana);
            }
        });
    }
}
