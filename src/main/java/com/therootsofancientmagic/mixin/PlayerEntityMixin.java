package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
// import com.therootsofancientmagic.item.robe.ElementalRobeItem;
import com.therootsofancientmagic.item.ModItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// import net.minecraft.particle.ParticleTypes;
// import net.minecraft.server.world.ServerWorld;

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

    @Unique
    private int fireRobeTimer = 0;

    @Unique
    private int fireRobeCooldown = 0;

    @Unique
    private boolean isFireRobeOnBack() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null) {
                ItemStack backStack = inv.getStack(0);
                return !backStack.isEmpty() && backStack.isOf(ModItem.FIRE_ROBE);
            }
        }
        return false;
    }

    @Override
    public void activateFireRobeAbility() {
        if (isFireRobeOnBack() && this.fireRobeCooldown <= 0) {
            this.fireRobeTimer = 140; 
            this.fireRobeCooldown = 240;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyFireRobeEffects(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.getWorld().isClient()) {
            if (this.fireRobeCooldown > 0) {
                this.fireRobeCooldown--;
            }

            if (this.fireRobeTimer > 0 && isFireRobeOnBack()) {
                this.fireRobeTimer--;

                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED, 20, 0, true, false
                ));

                if (this.fireRobeTimer % 2 == 0) {
                    ServerWorld serverWorld = (ServerWorld) player.getWorld();
                    serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        player.getX(),
                        player.getBodyY(0.5),
                        player.getZ(),
                        4, 0.3, 0.5, 0.3, 0.02
                    );
                }
            }
        }
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void onTakeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.getWorld().isClient()) {
            Entity attacker = source.getAttacker();

            if (this.fireRobeTimer > 0 && isFireRobeOnBack() && attacker instanceof LivingEntity livingAttacker) {
                livingAttacker.setOnFireFor(4);
            }
        }
    }
}

