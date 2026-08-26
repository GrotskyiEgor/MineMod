package com.therootsofancientmagic.item.tools.unique_tools;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.ProjectileUtil;

public class LightningHammerItem extends SwordItem {

    private static final int COOLDOWN_TICKS = 100;
    private static final int MANA_COST = 30;
    private static final double RAYCAST_DISTANCE = 20.0D;

    public LightningHammerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    // При ударе дает Слабость 2 
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient()) {
            // Накладывает Слабость II на врага при ударе на 6 секунд (120 тиков)
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 1));
        }
        return super.postHit(stack, target, attacker);
    }

    // Механика призыва молнии
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            
            // Тратит 30 мани
            if (!serverPlayer.isCreative() 
                    && !PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, MANA_COST, serverPlayer)) {
                // Если маны нет,способность не сработает
                return TypedActionResult.fail(stack);
            }

            // Запускает рейкаст по взгляду игрока
            Vec3d cameraPos = serverPlayer.getEyePos();
            Vec3d rotationVec = serverPlayer.getRotationVec(1.0F);
            Vec3d targetPos = cameraPos.add(rotationVec.multiply(RAYCAST_DISTANCE));

            // Делает рейкаст на блоки
            HitResult blockHit = world.raycast(new net.minecraft.world.RaycastContext(
                    cameraPos, targetPos,
                    net.minecraft.world.RaycastContext.ShapeType.OUTLINE,
                    net.minecraft.world.RaycastContext.FluidHandling.NONE,
                    serverPlayer
            ));

            BlockPos strikePos = null;

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                strikePos = ((BlockHitResult) blockHit).getBlockPos();
            } 
            else {
                strikePos = BlockPos.ofFloored(targetPos);
            }

            // Рейкаст для мобов
            HitResult entityHit = ProjectileUtil.raycast(
                    serverPlayer, cameraPos, targetPos,
                    serverPlayer.getBoundingBox().stretch(rotationVec.multiply(RAYCAST_DISTANCE)).expand(1.0D, 1.0D, 1.0D),
                    entity -> entity instanceof LivingEntity,
                    RAYCAST_DISTANCE * RAYCAST_DISTANCE
            );

            if (entityHit != null && entityHit.getType() == HitResult.Type.ENTITY) {
                strikePos = ((EntityHitResult) entityHit).getEntity().getBlockPos();
            }

            if (strikePos != null) {
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(strikePos));
                    lightning.setChanneler(serverPlayer); 
                    world.spawnEntity(lightning);
                }
            }

            // Задает для молота кд 5 сеунд
            serverPlayer.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            return TypedActionResult.success(stack, false);
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}