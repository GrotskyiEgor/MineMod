package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class) // Внедряемся в базов ий класс всех сущностей в Майнкрафте
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    // Кастомний карман для NBT тегов ман и
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return this.persistentData;
    }

    // Инжектим логику записи данн их: сохраняем наш карман при в иходе игрока из мира
    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (this.persistentData != null) {
            nbt.put("therootsofancientmagic.mana_data", this.persistentData);
        }
    }

    // Инжектим логику чтения данн их: загружаем ману обратно, когда игрок заходит на сервер
    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("therootsofancientmagic.mana_data", 10)) {
            this.persistentData = nbt.getCompound("therootsofancientmagic.mana_data");
        }
    }
}
