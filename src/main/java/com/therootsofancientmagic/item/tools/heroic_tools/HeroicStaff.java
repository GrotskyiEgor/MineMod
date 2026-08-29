package com.therootsofancientmagic.item.tools.heroic_tools;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HeroicStaff extends Item {
	private static final int COOLDOWN_TICKS = 100; 
	private static final int SCAN_RADIUS = 10;
	// Радиус коробки 2 означает, что коробка будет размером 5х5х5 блоков
	private static final int BOX_RADIUS = 2; 

	public HeroicStaff(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (user.getItemCooldownManager().isCoolingDown(this)) {
			return TypedActionResult.fail(stack);
		}

		if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
			// Потребление 50 единиц маны
			if (!PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 50, serverPlayer)) {
				return TypedActionResult.fail(stack);
			}

			ServerWorld serverWorld = (ServerWorld) world;
			BlockPos userPos = user.getBlockPos();

			// Находим всех мобов и игроков в радиусе 10 блоков вокруг использующего
			Box scanBox = new Box(userPos).expand(SCAN_RADIUS);
			List<LivingEntity> targets = serverWorld.getEntitiesByClass(LivingEntity.class, scanBox, entity -> entity != user);

			if (targets.isEmpty()) {
				return TypedActionResult.fail(stack);
			}

			// Применяем магию к каждой найденной цели
			for (LivingEntity target : targets) {
				// Центрируем коробку по ногам моба
				BlockPos targetPos = target.getBlockPos(); 

				createObsidianBoxWithLava(serverWorld, targetPos);
				applyTargetEffects(target);
				spawnDragonParticles(serverWorld, targetPos);
			}

			// Звук каста заклинания
			serverWorld.playSound(null, userPos, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.0F, 0.8F);
			user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
		}

		return TypedActionResult.success(stack, world.isClient);
	}

	/**
	 * Генерирует обсидиановую коробку (куб), заполненную лавой внутри.
	 */
	private void createObsidianBoxWithLava(ServerWorld world, BlockPos centerPos) {
		for (int x = -BOX_RADIUS; x <= BOX_RADIUS; x++) {
			for (int y = -BOX_RADIUS; y <= BOX_RADIUS; y++) {
				for (int z = -BOX_RADIUS; z <= BOX_RADIUS; z++) {
					BlockPos currentPos = centerPos.add(x, y, z);

					if (!world.isInBuildLimit(currentPos)) {
						continue;
					}

					// Проверяем, является ли блок внешней стеной куба (коробки)
					boolean isOuterWall = Math.abs(x) == BOX_RADIUS || Math.abs(y) == BOX_RADIUS || Math.abs(z) == BOX_RADIUS;

					if (isOuterWall) {
						// Заменяем только уничтожаемые блоки (чтобы не стереть бедрок или чужой приват)
						if (world.getBlockState(currentPos).isReplaceable() || world.getBlockState(currentPos).isLiquid()) {
							world.setBlockState(currentPos, Blocks.OBSIDIAN.getDefaultState());
						}
					} else {
						// Всё, что внутри коробки, заполняем лавой
						world.setBlockState(currentPos, Blocks.LAVA.getDefaultState());
					}
				}
			}
		}
	}

	/**
	 * Накладывает эффекты Иссушения, Слабости и Подсветки прямо на саму цель.
	 */
	private void applyTargetEffects(LivingEntity target) {
		// Подсветка (Glowing) на 10 секунд (200 тиков)
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0, false, false));
		// Иссушение II (Wither) на 8 секунд (160 тиков)
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 160, 1));
		// Слабость II (Weakness) на 8 секунд (160 тиков)
		target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 160, 1));
	}

	/**
	 * Спавнит фиолетовые частицы вокруг коробки в момент создания.
	 */
	private void spawnDragonParticles(ServerWorld world, BlockPos centerPos) {
		world.spawnParticles(
				ParticleTypes.DRAGON_BREATH, 
				centerPos.getX() + 0.5, 
				centerPos.getY() + 1.0, 
				centerPos.getZ() + 0.5, 
				40, 1.5, 1.5, 1.5, 0.1
		);
	}
}
