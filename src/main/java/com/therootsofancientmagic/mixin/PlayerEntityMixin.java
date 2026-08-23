package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.item.robe.ElementalRobeItem;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements CustomArmorHolder {

    @Unique
    private final SimpleInventory customArmorInventory = new SimpleInventory(4);

    @Unique
    private int fireRobeTimer = 0;

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

            boolean isFireRobeEquipped = false;

            for (int i = 0; i < 4; i++) {
                ItemStack stack = this.customArmorInventory.getStack(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ElementalRobeItem) {
                    robeCount++;
                }

                if (!stack.isEmpty() && stack.isOf(ModItem.FIRE_ROBE)) {
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

            if (isFireRobeEquipped && this.fireRobeTimer > 0) {
                this.fireRobeTimer--;
                if (this.fireRobeTimer %2 == 0) {
                    ((ServerWorld) player.getWorld()).spawnParticles(
                        ParticleTypes.FLAME, 
                        player.getX() + (player.getRandom().nextDouble() - 0.5),
                        player.getY() + (player.getRandom().nextDouble() * 2),
                        player.getZ() + (player.getRandom().nextDouble() - 0.5),
                        1, 0, 0.02, 0, 0.01
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

            if(attacker instanceof LivingEntity LivingAttacker && this.fireRobeTimer > 0) {
                LivingAttacker.setOnFireFor(4);
            }
        }
    }

    @Unique
    private boolean isAquaRobeOnBack() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null) {
                ItemStack backStack = inv.getStack(0);
                return !backStack.isEmpty() && backStack.isOf(ModItem.AQUA_ROBE);
            }
        }
        return false;
    }

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
    public void ActivateAquaRobeAbility() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (isAquaRobeOnBack() && !player.getItemCooldownManager().isCoolingDown(ModItem.AQUA_ROBE)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 600, 0, true, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0, true, false));
            player.getItemCooldownManager().set(ModItem.AQUA_ROBE, 700);
        }
    }

    @Override
    public void ActivateFireRobeAbility() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (isFireRobeOnBack() && !player.getItemCooldownManager().isCoolingDown(ModItem.FIRE_ROBE)) {
            this.fireRobeTimer = 200; 
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0, true, false));
            player.getItemCooldownManager().set(ModItem.FIRE_ROBE, 300); 
        }
    }
}