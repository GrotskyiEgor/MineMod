package com.therootsofancientmagic.item.tools.unique_tools;

import com.therootsofancientmagic.mana.PlayerMana;
import com.therootsofancientmagic.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.List;

public class BattleAxeItem extends SwordItem {

    // Радиус абилки(как торнадо)
    private static final double SPIN_RADIUS = 3.5D;
    // Кулдаун в тиках: 10 секунд * 20 тиков = 200
    private static final int COOLDOWN_TICKS = 200;

    public BattleAxeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    // Задает тип анимации, заставляет держать топор в руке пока идет абилка
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE; 
    }

    // Абилка длиться 5 секунд
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 100;
    }

    // Срабативает когда нажимаеться правая кнопка миши
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Проверяем кулдаун перед активацией
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        if (user instanceof ServerPlayerEntity serverPlayer) {
            // Проверяет, если есть 20 мани
            if (!serverPlayer.isCreative()
                    && !PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 20, serverPlayer)) {
                // Если нету мани то абилки не будет
                return TypedActionResult.fail(stack);
            }
        }

        // Похоже с типом анимации, но ета функция удерживает топор в руке
        user.setCurrentHand(hand);
        return TypedActionResult.success(stack, world.isClient());
    }

    // Главний цикл, сервер постоянно тикает когда зажата абилка
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTime) {
        if (user instanceof PlayerEntity player) {
            
            // При абилке на игрока налаживаеться ефект замедления и сопротивления
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 0, false, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, 0, false, false, false));

            // Расщитивает сколько секунд/тиков осталось до завершения абилки
            int ticksUsed = getMaxUseTime(stack) - remainingUseTime;

            // Каждий тик вращает игрока на 45 градусов
            float currentBodyYaw = player.getYaw() + (ticksUsed * 45.0F);
            player.setBodyYaw(currentBodyYaw);
            player.setHeadYaw(currentBodyYaw);

            // Вращение частиц (Выполняется только на стороне клиента для безопасности)
            if (world.isClient()) {
                double angle = ticksUsed * 0.4D; 
                double px = player.getX() + Math.cos(angle) * 2.5D;
                double pz = player.getZ() + Math.sin(angle) * 2.5D;
                world.addParticle(ParticleTypes.SWEEP_ATTACK, px, player.getY() + 0.8D, pz, 0, 0, 0);
            }
            
            if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
                // Каждую секунду абилки, списивает 10 мани
                if (ticksUsed % 20 == 0 && ticksUsed > 0) {
                    if (!serverPlayer.isCreative() && !PlayerMana.consumeMana((IEntityDataSaver) serverPlayer, 10, serverPlayer)) {
                        // Если мани нет, абилка прекращаеться
                        player.clearActiveItem();
                        return;
                    }
                }

                if (ticksUsed % 10 == 0) {
                    Vec3d playerPos = player.getPos();
                    
                    // Создает 5д куб в котором наноситься урон при обнаружения мобов в нем
                    Box searchBox = new Box(
                            playerPos.x - SPIN_RADIUS, playerPos.y - 1.0D, playerPos.z - SPIN_RADIUS,
                            playerPos.x + SPIN_RADIUS, playerPos.y + 2.0D, playerPos.z + SPIN_RADIUS
                    );
                    // СОбирает список врагов в кубе
                    List<Entity> targets = world.getOtherEntities(player, searchBox, 
                            entity -> entity instanceof LivingEntity && entity.distanceTo(player) <= SPIN_RADIUS);

                    // Наносит урон каждому мобу в кубе
                    for (Entity entity : targets) {
                        LivingEntity target = (LivingEntity) entity;
                        // Наносит весь урон топора
                        target.damage(world.getDamageSources().playerAttack(player), this.getAttackDamage());

                        // При нанесении урона отбрасивает врагов от игрока
                        Vec3d pushDirection = target.getPos().subtract(playerPos).normalize().multiply(0.4D);
                        target.addVelocity(pushDirection.x, 0.1D, pushDirection.z);
                        target.velocityModified = true;
                    }
                    // Звук
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 
                            SoundCategory.PLAYERS, 1.0F, 0.8F);
                    // Партикли
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.CLOUD, 
                                player.getX(), player.getY() + 0.2D, player.getZ(), 
                                5, 1.2D, 0.1D, 1.2D, 0.05D);
                    }
                }
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTime) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
        }
        super.onStoppedUsing(stack, world, user, remainingUseTime);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            // 10 секунд кд для топора, если способность полностью завершилась
            player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
        }
        return super.finishUsing(stack, world, user);
    }
}