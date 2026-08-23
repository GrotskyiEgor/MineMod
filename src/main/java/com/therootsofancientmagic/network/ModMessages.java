package com.therootsofancientmagic.network;

import com.therootsofancientmagic.network.ManaSyncS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModMessages {
    // Создает уникальний айди канал
    public static final Identifier MANA_SYNC_ID = new Identifier("the-roots-of-ancient-magic", "mana_sync");

    // Метод для регистрации приемников пакетов
    public static void registerC2SPackets() {
    }

    // Регистрирует пакет на клиенте
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(MANA_SYNC_ID, ManaSyncS2CPacket::receive);
    }

    public static void sendToClient(ServerPlayerEntity player, int mana) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(mana);
        
        // Отправляет пакет по каналу конкретному игроку
        ServerPlayNetworking.send(player, MANA_SYNC_ID, buf);
    }
}
