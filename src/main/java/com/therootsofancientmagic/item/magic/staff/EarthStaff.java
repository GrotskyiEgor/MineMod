package com.therootsofancientmagic.item.magic.staff;

import com.therootsofancientmagic.mana.PlayerMana; // Импортируем нашу систему мани
import com.therootsofancientmagic.util.IEntityDataSaver; // Импортируем кармашек NBT данних
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity; // Добавили серверного игрока для пакетов мани
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class EarthStaff extends Item {
	private static final int COOLDOWN_TICKS = 15;
	private static final int SPHERE_RADIUS = 2;
	private static final double RAY_RANGE = 20.0D;

	public EarthStaff(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (user.getItemCooldownManager().isCoolingDown(this)) {
			return TypedActionResult.fail(stack);
		}

		if (!world.isClient) {
			if (user instanceof ServerPlayerEntity serverPlayer) {
				
				if (PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
					

					createStoneSphere(world, user);

					world.playSound(
							null,
							user.getBlockPos(),
							SoundEvents.BLOCK_STONE_PLACE,
							SoundCategory.PLAYERS,
							1.0F,
							1.0F
					);

					user.getItemCooldownManager().set(this, COOLDOWN_TICKS);

				} else {
					return TypedActionResult.fail(stack);
				}
			}
		}

		return TypedActionResult.success(stack, world.isClient);
	}

	private void createStoneSphere(World world, PlayerEntity user) {
		BlockPos bottomPos = getTargetPoint(world, user);
		BlockPos centerPos = bottomPos.up(SPHERE_RADIUS);

		for (int x = -SPHERE_RADIUS; x <= SPHERE_RADIUS; x++) {
			for (int y = -SPHERE_RADIUS - 1; y <= SPHERE_RADIUS; y++) {
				for (int z = -SPHERE_RADIUS; z <= SPHERE_RADIUS; z++) {
					double distance = Math.sqrt(x * x + y * y + z * z);
					if (distance < SPHERE_RADIUS - 0.5D
							|| distance > SPHERE_RADIUS + 0.5D) {
						continue;
					}

					if (isSecondLayerOpening(x, y, z)) {
						continue;
					}

					BlockPos blockPos = centerPos.add(x, y, z);
					if (world.getBlockState(blockPos).isReplaceable()) {
						world.setBlockState(blockPos, Blocks.STONE.getDefaultState());
					}
				}
			}
		}
	}

	private boolean isSecondLayerOpening(int x, int y, int z) {
		return y == 0 && ((Math.abs(x) == SPHERE_RADIUS && z == 0) || (Math.abs(z) == SPHERE_RADIUS && x == 0));
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
