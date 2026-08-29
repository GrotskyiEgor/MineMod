package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class AquaStaff extends Item {
	private static final int COOLDOWN_TICKS = 15;
	private static final int SPHERE_RADIUS = 2;
	private static final double RAY_RANGE = 20.0D;
	private static final int DEBRIS_COUNT = 150;
	private static final double KNOCKBACK_STRENGTH = 10.0;

	private static final Random RANDOM = new Random();

	public AquaStaff(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (user.getItemCooldownManager().isCoolingDown(this)) {
			return TypedActionResult.fail(stack);
		}

		if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
			if (!PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
				return TypedActionResult.fail(stack);
			}

			BlockPos bottomPos = getTargetPoint(world, user);
			BlockPos centerPos = bottomPos.up(SPHERE_RADIUS);

			// Создаем купол воды или льда (в зависимости от твоей задумки, тут изначальный вызов сферы)
			createWaterSphere(world, centerPos);
			
			ServerWorld serverWorld = (ServerWorld) world;
			
			// Эффекты водяного взрыва и расталкивание мобов
			knockbackNearbyEntities(serverWorld, centerPos, user);
			spawnImpactEffects(serverWorld, centerPos);
			
			// Спавн СИНЕГО водяного дыхания прямо внутри купола
			spawnAquaBreathCloud(serverWorld, centerPos, user);

			world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
		}

		return TypedActionResult.success(stack, world.isClient);
	}

	private void createWaterSphere(World world, BlockPos centerPos) {
		for (int x = -SPHERE_RADIUS; x <= SPHERE_RADIUS; x++) {
			for (int y = -SPHERE_RADIUS - 1; y <= SPHERE_RADIUS; y++) {
				for (int z = -SPHERE_RADIUS; z <= SPHERE_RADIUS; z++) {
					double distance = Math.sqrt(x * x + y * y + z * z);
					if (distance < SPHERE_RADIUS - 0.5D || distance > SPHERE_RADIUS + 0.5D) {
						continue;
					}
					if (isSecondLayerOpening(x, y, z)) {
						continue;
					}

					BlockPos blockPos = centerPos.add(x, y, z);
					if (world.getBlockState(blockPos).isReplaceable()) {
						// Здесь можно ставить блоки льда или синего стекла (вместо STONE)
						world.setBlockState(blockPos, Blocks.ICE.getDefaultState());
					}
				}
			}
		}
	}

	private boolean isSecondLayerOpening(int x, int y, int z) {
		return y == 0 && ((Math.abs(x) == SPHERE_RADIUS && z == 0) || (Math.abs(z) == SPHERE_RADIUS && x == 0));
	}

	/**
	 * Создает синее бурлящее водяное облако на месте купола.
	 */
	private void spawnAquaBreathCloud(ServerWorld world, BlockPos centerPos, PlayerEntity user) {
		AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, centerPos.getX() + 0.5, centerPos.getY(), centerPos.getZ() + 0.5);
		
		cloud.setOwner(user);
		cloud.setParticleType(ParticleTypes.ENTITY_EFFECT); // Идеальный тип для кастомного цвета
		cloud.setRadius((float) SPHERE_RADIUS + 0.5F);
		cloud.setRadiusOnUse(-0.2F);
		cloud.setWaitTime(0);
		cloud.setDuration(120); // Водяной туман висит 6 секунд
		cloud.setRadiusGrowth(-0.02F);
		
		// Цвет: Насыщенный магический синий (0x0066FF)
		cloud.setColor(0x0066FF); 
		
		// Эффекты: Сильное замедление (тяжело двигаться в воде) и урон от удушья (Иссушение)
		cloud.addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
		cloud.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1)); 

		world.spawnEntity(cloud);
	}

	/**
	 * Водяные брызги и капли разлетаются взрывом от центра купола.
	 */
	private void spawnImpactEffects(ServerWorld world, BlockPos centerPos) {
		Vec3d center = Vec3d.ofCenter(centerPos);
		
		// Частицы брызг и капель воды
		sphereBurst(world, center, DEBRIS_COUNT, ParticleTypes.SPLASH, 0.4);
		sphereBurst(world, center, 80, ParticleTypes.FALLING_WATER, 0.3);
		sphereBurst(world, center, DEBRIS_COUNT / 2, ParticleTypes.CLOUD, 0.15);
		groundRing(world, center, SPHERE_RADIUS + 1.5, 40);

		world.playSound(null, centerPos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1.2F, 0.7F);
		world.playSound(null, centerPos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.PLAYERS, 1.0F, 1.2F);
	}

	/**
	 * Отбрасывает существ мощной волной от центра купола.
	 */
	private void knockbackNearbyEntities(ServerWorld world, BlockPos centerPos, PlayerEntity user) {
		Box box = new Box(centerPos).expand(SPHERE_RADIUS + 1.0);
		List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, e -> e != user);

		for (LivingEntity entity : entities) {
			double dx = entity.getX() - (centerPos.getX() + 0.5);
			double dz = entity.getZ() - (centerPos.getZ() + 0.5);
			double distance = Math.max(Math.sqrt(dx * dx + dz * dz), 0.1);

			dx /= distance;
			dz /= distance;

			entity.takeKnockback(KNOCKBACK_STRENGTH, -dx, -dz);
			Vec3d velocity = entity.getVelocity();
			entity.setVelocity(velocity.x, 0.4, velocity.z);
			entity.velocityModified = true;
		}
	}

	/** Разлёт частиц равномерно по сфере. */
	private void sphereBurst(ServerWorld world, Vec3d origin, int count, ParticleEffect particle, double maxSpeed) {
		for (int i = 0; i < count; i++) {
			double u = RANDOM.nextDouble() * 2.0 - 1.0;
			double theta = RANDOM.nextDouble() * Math.PI * 2.0;
			double sqrtTerm = Math.sqrt(1.0 - u * u);

			double dirX = sqrtTerm * Math.cos(theta);
			double dirY = u;
			double dirZ = sqrtTerm * Math.sin(theta);
			double speed = RANDOM.nextDouble() * maxSpeed;

			world.spawnParticles(particle, origin.x, origin.y, origin.z, 1, dirX * speed, dirY * speed, dirZ * speed, 1.0);
		}
	}

	/** Кольцо брызг на земле вокруг купола. */
	private void groundRing(ServerWorld world, Vec3d center, double radius, int count) {
		for (int i = 0; i < count; i++) {
			double angle = (Math.PI * 2.0 * i) / count;
			double dirX = Math.cos(angle);
			double dirZ = Math.sin(angle);

			double x = center.x + dirX * radius;
			double z = center.z + dirZ * radius;

			world.spawnParticles(ParticleTypes.FISHING, x, center.y - SPHERE_RADIUS, z, 1, dirX * 0.05, 0.05, dirZ * 0.05, 0.0);
		}
	}

	private BlockPos getTargetPoint(World world, PlayerEntity player) {
		Vec3d start = player.getCameraPosVec(1.0F);
		Vec3d end = start.add(player.getRotationVec(1.0F).multiply(RAY_RANGE));
		BlockHitResult hitResult = world.raycast(new RaycastContext(
				start,
				end,
				RaycastContext.ShapeType.OUTLINE,
				RaycastContext.FluidHandling.NONE,
				player
		));

		if (hitResult.getType() == HitResult.Type.BLOCK) {
			return BlockPos.ofFloored(hitResult.getPos());
		}

		return BlockPos.ofFloored(end);
	}
}
