package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.robe.RobeItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements CustomArmorHolder {

    @Unique
    private final SimpleInventory customArmorInventory = new SimpleInventory(4);

    @Override
    public Inventory getCustomArmorInventory() {
        return this.customArmorInventory;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomArmorToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("CustomArmorInventory", this.customArmorInventory.toNbtList());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomArmorFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CustomArmorInventory", 9)) {
            NbtList nbtList = nbt.getList("CustomArmorInventory", 10);
            this.customArmorInventory.readNbtList(nbtList);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyRobeEffects(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.getWorld().isClient()) {
            int robeCount = 0;

            for (int i = 0; i < 4; i++) {
                ItemStack stack = this.customArmorInventory.getStack(i);
                if (!stack.isEmpty() && stack.getItem() instanceof RobeItem) {
                    robeCount++;
                }
            }

            if (robeCount > 0) {
                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED, 
                    20,           
                    robeCount - 1,
                    true,           
                    false          
                ));
            }
        }
    }
}