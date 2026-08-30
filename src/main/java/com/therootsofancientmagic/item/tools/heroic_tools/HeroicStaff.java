package com.therootsofancientmagic.item.tools.heroic_tools;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class HeroicStaff extends Item {
	private static final int COOLDOWN_TICKS = 1200;
	private static final int DOME_RADIUS = 2;
	private static final double DETECTION_RADIUS = 10.0D;
	private static final int DEBRIS_COUNT = 150;

	private static final Random RANDOM = new Random();

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
			ServerWorld serverWorld = (ServerWorld) world;

			List<LivingEntity> targets = findNearbyEntities(serverWorld, user);

			if (targets.isEmpty()) {
				return TypedActionResult.fail(stack);
			}

			if (!PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 100, serverPlayer)) {
				return TypedActionResult.fail(stack);
			}

			for (LivingEntity target : targets) {
				BlockPos centerPos = target.getBlockPos().up(DOME_RADIUS);

				createObsidianSnowSphere(serverWorld, centerPos);
				spawnImpactEffects(serverWorld, centerPos);
				spawnDragonBreathCloud(serverWorld, centerPos, user);
			}

			world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
		}

		return TypedActionResult.success(stack, world.isClient);
	}

	private List<LivingEntity> findNearbyEntities(ServerWorld world, PlayerEntity user) {
		Box box = new Box(user.getBlockPos()).expand(DETECTION_RADIUS);
		return world.getEntitiesByClass(
				LivingEntity.class,
				box,
				e -> e != user && e.squaredDistanceTo(user) <= DETECTION_RADIUS * DETECTION_RADIUS
		);
	}

	private void createObsidianSnowSphere(World world, BlockPos centerPos) {
		for (int x = -DOME_RADIUS; x <= DOME_RADIUS; x++) {
			for (int y = -DOME_RADIUS; y <= DOME_RADIUS; y++) {
				for (int z = -DOME_RADIUS; z <= DOME_RADIUS; z++) {
					double distance = Math.sqrt(x * x + y * y + z * z);
					if (distance > DOME_RADIUS + 0.5D) {
						continue;
					}

					BlockPos blockPos = centerPos.add(x, y, z);

					if (distance >= DOME_RADIUS - 0.5D) {
						world.setBlockState(blockPos, Blocks.OBSIDIAN.getDefaultState());
					} else {
						world.setBlockState(blockPos, Blocks.SNOW_BLOCK.getDefaultState());
					}
				}
			}
		}
	}

	/**
	 * Создаёт облако дыхания дракона внутри купола: Слабость III и Иссушение.
	 */
	private void spawnDragonBreathCloud(ServerWorld world, BlockPos centerPos, PlayerEntity user) {
		AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, centerPos.getX() + 0.5, centerPos.getY(), centerPos.getZ() + 0.5);

		cloud.setOwner(user);
		cloud.setParticleType(ParticleTypes.DRAGON_BREATH);
		cloud.setRadius((float) DOME_RADIUS + 0.5F);
		cloud.setRadiusOnUse(-0.2F);
		cloud.setWaitTime(0);
		cloud.setDuration(200);
		cloud.setRadiusGrowth(-0.02F);

		cloud.addEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 2));
		cloud.addEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));

		world.spawnEntity(cloud);
	}

	private void spawnImpactEffects(ServerWorld world, BlockPos centerPos) {
		Vec3d center = Vec3d.ofCenter(centerPos);
		ParticleEffect obsidianDust = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.OBSIDIAN.getDefaultState());
		ParticleEffect snowDust = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SNOW_BLOCK.getDefaultState());

		sphereBurst(world, center, DEBRIS_COUNT, obsidianDust, 0.4);
		sphereBurst(world, center, 100, snowDust, 0.35);
		sphereBurst(world, center, DEBRIS_COUNT / 3, ParticleTypes.SNOWFLAKE, 0.15);
		groundRing(world, center, DOME_RADIUS + 1.5, 40);

		world.playSound(null, centerPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.2F, 0.5F);
		world.playSound(null, centerPos, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 0.8F);
	}

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

	private void groundRing(ServerWorld world, Vec3d center, double radius, int count) {
		for (int i = 0; i < count; i++) {
			double angle = (Math.PI * 2.0 * i) / count;
			double dirX = Math.cos(angle);
			double dirZ = Math.sin(angle);

			double x = center.x + dirX * radius;
			double z = center.z + dirZ * radius;

			world.spawnParticles(ParticleTypes.CRIT, x, center.y - DOME_RADIUS, z, 1, dirX * 0.05, 0.05, dirZ * 0.05, 0.0);
		}
	}
}