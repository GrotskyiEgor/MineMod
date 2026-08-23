package com.therootsofancientmagic.util;
import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    // Этот метод будет возвращать скр ит ий карман NBT данн их у игрока
    NbtCompound getPersistentData();
}
