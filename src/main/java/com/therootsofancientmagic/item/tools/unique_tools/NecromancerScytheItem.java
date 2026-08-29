package com.therootsofancientmagic.item.tools.unique_tools;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class NecromancerScytheItem extends SwordItem {

    // Абилка стоит 50 мани, тоесть 5 орбов
    private static final int MANA_COST = 50;
    // кулдаун на использование, 15 секунд
    private static final int COOLDOWN_TICKS = 300;

    public NecromancerScytheItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    // Накладывает пассивние эффекти каждие 20 тиков, пока коса находится в руках
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof net.minecraft.entity.LivingEntity livingEntity) {
            
            // Проверяет активний слот хотбара или левую руку
            boolean isHoldingInMainHand = selected;
            boolean isHoldingInOffHand = livingEntity.getOffHandStack() == stack;

            if (isHoldingInMainHand || isHoldingInOffHand) {
                // Обновляет Ночное зрение и Скорость I на 2 тика для мгновенного снятия при свапе слота
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2, 0, false, false, true));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2, 0, false, false, true));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Проверяет кулдаун коси
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        // Запускает руку и ставит локальний кулдаун на клиенте для синхронизации слотов
        if (world.isClient()) {
            user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            return TypedActionResult.success(stack);
        }

        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            
           // Тратит 50 мани при использовании абилки
            if (!serverPlayer.isCreative() 
                    && !PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, MANA_COST, serverPlayer)) {
                // Снимает кд на клиенте, если мани не хватило для запуска
                serverPlayer.getItemCooldownManager().remove(this);
                return TypedActionResult.fail(stack);
            }

            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos playerPos = serverPlayer.getBlockPos();

            // Призивает 2 визер скелета
            for (int i = 0; i < 2; i++) {
                WitherSkeletonEntity witherSkeleton = EntityType.WITHER_SKELETON.create(world);
                if (witherSkeleton != null) {
                    setupSummon(witherSkeleton, serverPlayer, serverWorld, playerPos);
                }
            }

            // Призивает 2 зомби
            for (int i = 0; i < 2; i++) {
                ZombieEntity zombie = EntityType.ZOMBIE.create(world);
                if (zombie != null) {
                    setupSummon(zombie, serverPlayer, serverWorld, playerPos);
                }
            }

            // Призивает 1 скелета 
            SkeletonEntity skeleton = EntityType.SKELETON.create(world);
            if (skeleton != null) {
                skeleton.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                setupSummon(skeleton, serverPlayer, serverWorld, playerPos);
            }
            
            world.playSound(null, playerPos, SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 1.0F, 0.5F);
            serverWorld.spawnParticles(ParticleTypes.WITCH, serverPlayer.getX(), serverPlayer.getY() + 1.0D, serverPlayer.getZ(), 30, 1.5D, 0.5D, 1.5D, 0.1D);
            
            // Активирует кд
            serverPlayer.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    // Главний ии у мобов, чтоби они не атаковали игрока которий их призвал
    private void setupSummon(PathAwareEntity summon, ServerPlayerEntity master, ServerWorld world, BlockPos pos) {
        // Спавнит мобов в рандомном месте возле игрока чтоб не в 1 кучке
        double xOffset = master.getRandom().nextDouble() * 3.0D - 1.5D;
        double zOffset = master.getRandom().nextDouble() * 3.0D - 1.5D;
        
        summon.refreshPositionAndAngles(master.getX() + xOffset, master.getY(), master.getZ() + zOffset, master.getYaw(), master.getPitch());
        summon.initialize(world, world.getLocalDifficulty(pos), SpawnReason.MOB_SUMMONED, null, null);

        // Убирает полний таргет у мобов, они атаковать никого не будут
        summon.setTarget(null);
        
        try {
            java.lang.reflect.Field targetSelectorField = MobEntity.class.getDeclaredField("targetSelector");
            targetSelectorField.setAccessible(true);
            net.minecraft.entity.ai.goal.GoalSelector targetSelector = (net.minecraft.entity.ai.goal.GoalSelector) targetSelectorField.get(summon);

            if (targetSelector != null) {
                // Очищает базовий селектор целей через полученний селектор
                targetSelector.clear(goal -> true);

                // Моб будет атаковать тех кто его ударил(только не игрока)
                targetSelector.add(1, new RevengeGoal(summon) {
                    @Override
                    public boolean shouldContinue() {
                        if (summon.getAttacker() == master) {
                            summon.setAttacker(null);
                            return false;
                        }
                        return super.shouldContinue();
                    }

                    @Override
                    public void start() {
                        if (summon.getAttacker() == master) {
                            summon.setAttacker(null);
                            return;
                        }
                        super.start();
                    }
                }.setGroupRevenge(PlayerEntity.class));
                
                // Моб будет атаковать тех кого ударил игрок(которий их призвал)
                targetSelector.add(2, new ActiveTargetGoal<>(summon, MobEntity.class, true, (entity) -> {
                    if (entity == master || entity == summon) return false;
                    return entity.getAttacker() == master || master.getAttacking() == entity;
                }));
            }
        } catch (Exception e) {
            // Логирует ошибку в консоль
            e.printStackTrace();
        }

        world.spawnEntity(summon);
    }
}