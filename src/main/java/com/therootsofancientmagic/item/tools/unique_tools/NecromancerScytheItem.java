package com.therootsofancientmagic.item.tools.unique_tools;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
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
import java.lang.reflect.Field;

public class NecromancerScytheItem extends SwordItem {

    // Абилка стоит 50 мани, тоесть 5 орбов
    private static final int MANA_COST = 50;
    // кулдаун на использование, 15 секунд
    private static final int COOLDOWN_TICKS = 300;

    public NecromancerScytheItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Проверяет кулдаун коси
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }


        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            
           // Тратит 50 мани при использовании абилки
            if (!serverPlayer.isCreative() 
                    && !PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, MANA_COST, serverPlayer)) {
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
            return TypedActionResult.success(stack, false);
        }

        return TypedActionResult.success(stack, world.isClient());
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

            Field targetSelectorField = MobEntity.class.getDeclaredField("targetSelector");
            targetSelectorField.setAccessible(true);
            GoalSelector targetSelector = (GoalSelector) targetSelectorField.get(summon);

            if (targetSelector != null) {
                // Очищает все цели у моба
                targetSelector.clear(goal -> true);

                // Моб будет атаковать тех кто его ударил(только не игрока)
                targetSelector.add(1, new RevengeGoal(summon).setGroupRevenge(PlayerEntity.class));
                // Моб будет атаковать тех кого ударил игрок(которий их призвал)
                targetSelector.add(2, new ActiveTargetGoal<>(summon, MobEntity.class, true, (entity) -> {
                    if (entity == master || entity == summon) return false;

                    return entity.getAttacker() == master || master.getAttacking() == entity;
                }));
            }
        } catch (Exception e) {
            // Виводит ошибку если чтото пошло не так
            e.printStackTrace();
        }


        world.spawnEntity(summon);
    }
}
