package com.therootsofancientmagic.mana;

import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerMana {
    // Настройки системи мани
    private static final int MAX_MANA = 100;
    private static final int MANA_REGEN_TICK_DELAY = 20;

    // Метод добавления мани,например при регенерации или питье зелий
    public static void addMana(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int currentMana = nbt.getInt("mana");

        // Прибавляем ману но чтоб и она не прев исила максимальний лимит в 100 единиц
        if (currentMana + amount >= MAX_MANA) {
            nbt.putInt("mana", MAX_MANA);
        } else {
            nbt.putInt("mana", currentMana + amount);
        }
    }

    // Метод трати мани (этот метод твой тиммейт будет визивать внутри своих посохов!)
    public static boolean consumeMana(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int currentMana = nbt.getInt("mana");

        // Проверяем, хватает ли вообще мани игроку на заклинание
        if (currentMana >= amount) {
            // Тратит ману на посох
            nbt.putInt("mana", currentMana - amount); 
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

        // Проверяет, нужно ли регенить, если мана полная то таймер сбрасиваеться
        if (currentMana < MAX_MANA) {
            if (regenTimer >= MANA_REGEN_TICK_DELAY) {
                addMana(dataSaver, 1);
                nbt.putInt("mana_regen_timer", 0);
            } else {
                nbt.putInt("mana_regen_timer", regenTimer + 1);
            }
        } else {
            nbt.putInt("mana_regen_timer", 0);
        }
    }
}
