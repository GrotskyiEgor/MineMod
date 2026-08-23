package com.therootsofancientmagic.mana; // Путь к нашей папке mana

import com.therootsofancientmagic.util.IEntityDataSaver;
import com.therootsofancientmagic.network.ModMessages; // Импортируем нашу сетевую систему
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerMana {
    private static final int MAX_MANA = 100; 
    private static final int MANA_REGEN_TICK_DELAY = 200; 

    // Метод добавления мани
    public static void addMana(IEntityDataSaver player, int amount, ServerPlayerEntity serverPlayer) {
        NbtCompound nbt = player.getPersistentData();
        int currentMana = nbt.getInt("mana");
        int finalMana;

        // Прибавляем ману,но чтоби не било больше 100
        if (currentMana + amount >= MAX_MANA) {
            finalMana = MAX_MANA;
        } else {
            finalMana = currentMana + amount;
        }
        
        nbt.putInt("mana", finalMana);
        ModMessages.sendToClient(serverPlayer, finalMana);
    }

    // Метод трати мани
    public static boolean consumeMana(IEntityDataSaver player, int amount, ServerPlayerEntity serverPlayer) {
        NbtCompound nbt = player.getPersistentData();
        int currentMana = nbt.getInt("mana");


        if (currentMana >= amount) {
            int finalMana = currentMana - amount;
            nbt.putInt("mana", finalMana);
            
            ModMessages.sendToClient(serverPlayer, finalMana);
            return true;
        }
        return false;
    }

    // Пассивная регенерация мани
    public static void regenerateMana(ServerPlayerEntity player) {
        IEntityDataSaver dataSaver = (IEntityDataSaver) player;
        NbtCompound nbt = dataSaver.getPersistentData();
        
        int regenTimer = nbt.getInt("mana_regen_timer");
        int currentMana = nbt.getInt("mana");

        // Проверяем, нужно ли регенить ману
        if (currentMana < MAX_MANA) {
            if (regenTimer >= MANA_REGEN_TICK_DELAY) {
                addMana(dataSaver, 10, player); 
                nbt.putInt("mana_regen_timer", 0);
            } else {
                nbt.putInt("mana_regen_timer", regenTimer + 1);
            }
        } else {
            nbt.putInt("mana_regen_timer", 0);
        }
    }
}
