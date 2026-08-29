package com.therootsofancientmagic.mixin;

import com.therootsofancientmagic.component.CustomArmorHolder;
import com.therootsofancientmagic.entity.WindChargeEntity;
import com.therootsofancientmagic.item.ModItem;
import com.therootsofancientmagic.item.magic.robe.ElementalRobeItem;

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
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.tag.FluidTags;

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

    // @Unique
    // private boolean isFireRobeOnBack() {
    //     PlayerEntity player = (PlayerEntity) (Object) this;
    //     if (player instanceof CustomArmorHolder holder) {
    //         Inventory inv = holder.getCustomArmorInventory();
    //         if (inv != null) {
    //             ItemStack backStack = inv.getStack(0);
    //             return !backStack.isEmpty() && backStack.isOf(ModItem.FIRE_ROBE);
    //         }
    //     }
    //     return false;
    // }

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

    @Unique
    private boolean isFireRingInSlot(int slotIndex) {
    PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex < inv.size()) {
                ItemStack stack = inv.getStack(slotIndex);
                return !stack.isEmpty() && stack.isOf(ModItem.FIRE_RING);
            }
        }
        return false;
    }

    @Unique
    private boolean isAquaRingInSlot(int slotIndex) {
    PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex < inv.size()) {
                ItemStack stack = inv.getStack(slotIndex);
                return !stack.isEmpty() && stack.isOf(ModItem.AQUA_RING);
            }
        }
        return false;
    }

    @Unique
    private boolean isEarthRingInSlot(int slotIndex) {
    PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex < inv.size()) {
                ItemStack stack = inv.getStack(slotIndex);
                return !stack.isEmpty() && stack.isOf(ModItem.EARTH_RING);
            }
        }
        return false;
    }

    @Unique
    private boolean isAirRingInSlot(int slotIndex) {
    PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex < inv.size()) {
                ItemStack stack = inv.getStack(slotIndex);
                return !stack.isEmpty() && stack.isOf(ModItem.AIR_RING);
            }
        }
        return false;
    }

    @Unique
    public void castFireRingSpell() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        HitResult hitResult = player.raycast(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos targetPos = blockHit.getBlockPos().offset(blockHit.getSide());
            World world = player.getWorld();
            if (world.getBlockState(targetPos).isAir()) {
                world.setBlockState(targetPos, net.minecraft.block.Blocks.FIRE.getDefaultState());
                world.playSound(null, targetPos, net.minecraft.sound.SoundEvents.ITEM_FLINTANDSTEEL_USE,
                    net.minecraft.sound.SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.FLAME,
                    targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 15, 0.2, 0.2, 0.2, 0.05);
                }
                player.getItemCooldownManager().set(ModItem.FIRE_RING, 100);
            }
        }
    }
    
    @Unique
    public void castAquaRingSpell() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        HitResult hitResult = player.raycast(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos targetPos = blockHit.getBlockPos().offset(blockHit.getSide());
            World world = player.getWorld();
            if (world.getBlockState(targetPos).isAir()) {
                world.setBlockState(targetPos, net.minecraft.block.Blocks.WATER.getDefaultState());
                world.playSound(null, targetPos, net.minecraft.sound.SoundEvents.ITEM_BUCKET_EMPTY,
                    net.minecraft.sound.SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(ParticleTypes.SPLASH,
                    targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 30, 0.3, 0.3, 0.3, 0.1);
                }
                player.getItemCooldownManager().set(ModItem.AQUA_RING, 100);
            }
        }
    }

    @Unique
    public void castEarthRingSpell() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        HitResult hitResult = player.raycast(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos centerPos = blockHit.getBlockPos().offset(blockHit.getSide());
            World world = player.getWorld();
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    for (int z = 0; z < 2; z++) {
                        BlockPos targetPos = centerPos.add(x, y, z);
                        if (world.getBlockState(targetPos).isAir() || world.getBlockState(targetPos).isOf(net.minecraft.block.Blocks.WATER)) {
                            world.setBlockState(targetPos, net.minecraft.block.Blocks.DIRT.getDefaultState());
                        }
                    }
                }
            }

            world.playSound(null, centerPos, net.minecraft.sound.SoundEvents.BLOCK_GRAVEL_PLACE,
                net.minecraft.sound.SoundCategory.PLAYERS, 1.0F, 0.8F);

            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                    new net.minecraft.particle.BlockStateParticleEffect(ParticleTypes.BLOCK, net.minecraft.block.Blocks.DIRT.getDefaultState()),
                    centerPos.getX() + 1.0, centerPos.getY() + 1.0, centerPos.getX() + 1.0, 40, 0.5, 0.5, 0.5, 0.15);
            }

            player.getItemCooldownManager().set(ModItem.EARTH_RING, 100);
        }
    }

    @Unique
    public void castAirRingSpell() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();
        Vec3d look = player.getRotationVector();
        player.setVelocity(look.x * 2.0, look.y * 1.2 + 0.2, look.z * 2.0);
        player.velocityModified = true;
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
            world.playSound(null, player.getBlockPos(), net.minecraft.sound.SoundEvents.ENTITY_PHANTOM_FLAP,
                net.minecraft.sound.SoundCategory.PLAYERS, 1.0F, 1.4F);
            player.getItemCooldownManager().set(ModItem.AIR_RING, 100);
        }
    }

    @Override
    public void useFirstRingAbility() {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (isFireRingInSlot(2) && !player.getItemCooldownManager().isCoolingDown(ModItem.FIRE_RING)) {
            castFireRingSpell();
        } else if (isAquaRingInSlot(2) && !player.getItemCooldownManager().isCoolingDown(ModItem.AQUA_RING)) {
            castAquaRingSpell();
        } else if (isEarthRingInSlot(2) && !player.getItemCooldownManager().isCoolingDown(ModItem.EARTH_RING)) {
            castEarthRingSpell();
        } else if (isAirRingInSlot(2) && !player.getItemCooldownManager().isCoolingDown(ModItem.AIR_RING)) {
            castAirRingSpell();
        }

        
    }

    @Override
    public void useSecondRingAbility() {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (isFireRingInSlot(3) && !player.getItemCooldownManager().isCoolingDown(ModItem.FIRE_RING)) {
            castFireRingSpell();
        } else if (isAquaRingInSlot(3) && !player.getItemCooldownManager().isCoolingDown(ModItem.AQUA_RING)) {
            castAquaRingSpell();
        } else if (isEarthRingInSlot(3) && !player.getItemCooldownManager().isCoolingDown(ModItem.EARTH_RING)) {
            castEarthRingSpell();
        } else if (isAirRingInSlot(3) && !player.getItemCooldownManager().isCoolingDown(ModItem.AIR_RING)) {
            castAirRingSpell();
        }
    }

    @Unique
    private boolean isFireNecklaceInSlot(int slotIndex) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex >= 0 && slotIndex < inv.size()) {
                return inv.getStack(slotIndex).isOf(ModItem.FIRE_NECKLACE);
            }
        }
        return false;
    }

    @Unique
    private boolean isAirNecklaceInSlot(int slotIndex) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex >= 0 && slotIndex < inv.size()) {
                return inv.getStack(slotIndex).isOf(ModItem.AIR_NECKLACE);
            }
        }
        return false;
    }

    @Unique
    private boolean isEarthNecklaceInSlot(int slotIndex) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex >= 0 && slotIndex < inv.size()) {
                return inv.getStack(slotIndex).isOf(ModItem.EARTH_NECKLACE);
            }
        }
        return false;
    }

    @Unique
    private boolean isAquaNecklaceInSlot(int slotIndex) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player instanceof CustomArmorHolder holder) {
            Inventory inv = holder.getCustomArmorInventory();
            if (inv != null && slotIndex >= 0 && slotIndex < inv.size()) {
                return inv.getStack(slotIndex).isOf(ModItem.AQUA_NECKLACE);
            }
        }
        return false;
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void FireNecklacePassive(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        if (!world.isClient() && player.age % 10 == 0) {
            if (isFireNecklaceInSlot(1)) {
                if (!player.hasStatusEffect(StatusEffects.ABSORPTION)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, 0, true, false, true));
                }
            } else {
                if (player.hasStatusEffect(StatusEffects.ABSORPTION)) {
                    player.removeStatusEffect(StatusEffects.ABSORPTION);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void AirNecklacePassive(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        if (!world.isClient() && player.age % 10 == 0) {
            if (isAirNecklaceInSlot(1)) {
                if (!player.hasStatusEffect(StatusEffects.SPEED)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 210, 0, true, false, true));
                }
            } else {
                if (player.hasStatusEffect(StatusEffects.SPEED)) {
                    player.removeStatusEffect(StatusEffects.SPEED);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void EarthNecklacePassive(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        if (!world.isClient() && player.age % 10 == 0) {
            if (isEarthNecklaceInSlot(1)) {
                if (!player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 210, 0, true, false, true));
                }
            } else {
                if (player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                    player.removeStatusEffect(StatusEffects.JUMP_BOOST);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void AquaNecklacePassive(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        if (!world.isClient()) {
            if (isAquaNecklaceInSlot(1)) {
                if (player.getMaxAir() < 600) {
                    if (player.getAir() == player.getMaxAir()) {
                        player.setAir(600);
                    }
                }

                if (!player.isSubmergedIn(FluidTags.WATER) && player.getAir() < 600) {
                    player.setAir(600);
                }
            } else {
                if (player.getAir() > 300) {
                    player.setAir(300);
                }
            }
        }
    }
}

