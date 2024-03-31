package io.github.fabricators_of_create.porting_lib.event.common;

import java.util.Collection;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LivingEntityEvents {
	/**
	 * Legacy method will be removed in 1.20 use below method {@link LivingEntityEvents#EXPERIENCE_DROP_WITH_ENTITY}
	 */
	public static final Event<ExperienceDrop> EXPERIENCE_DROP = EventFactory.createArrayBacked(ExperienceDrop.class, callbacks -> (i, player) -> {
		for (ExperienceDrop callback : callbacks) {
			return callback.onLivingEntityExperienceDrop(i, player);
		}

		return i;
	});

	public static final Event<ExperienceDropNew> EXPERIENCE_DROP_WITH_ENTITY = EventFactory.createArrayBacked(ExperienceDropNew.class, callbacks -> (i, attackingPlayer, entity) -> {
		for (ExperienceDropNew callback : callbacks) {
			return callback.onLivingEntityExperienceDrop(i, attackingPlayer, entity);
		}

		return i;
	});

	public static final Event<KnockBackStrength> KNOCKBACK_STRENGTH = EventFactory.createArrayBacked(KnockBackStrength.class, callbacks -> (strength, player) -> {
		for (KnockBackStrength callback : callbacks) {
			return callback.onLivingEntityTakeKnockback(strength, player);
		}

		return strength;
	});

	public static final Event<Drops> DROPS = EventFactory.createArrayBacked(Drops.class, callbacks -> (target, source, drops) -> {
		for (Drops callback : callbacks) {
			if (callback.onLivingEntityDrops(target, source, drops)) {
				return true;
			}
		}

		return false;
	});

	public static final Event<Fall> FALL = EventFactory.createArrayBacked(Fall.class, callbacks -> (info) -> {
		for(Fall e : callbacks) {
			e.onFall(info);
			if (info.isCanceled()) {
				return;
			}
		}
	});

	public static final Event<LootingLevel> LOOTING_LEVEL = EventFactory.createArrayBacked(LootingLevel.class, callbacks -> (source, target, level, recent) -> {
		for (LootingLevel callback : callbacks) {
			int lootingLevel = callback.modifyLootingLevel(source, target, level, recent);
			if (lootingLevel != level) {
				return lootingLevel;
			}
		}

		return level;
	});

	public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, callbacks -> (entity) -> {
		for (Tick callback : callbacks) {
			callback.onLivingEntityTick(entity);
		}
	});

	/**
	 * Use {@link LivingEntityEvents#ACTUALLY_HURT} instead.
	 * Will be removed in 1.20
	 */
	@Deprecated(forRemoval = true, since = "1.19.2")
	public static final Event<Hurt> HURT = EventFactory.createArrayBacked(Hurt.class, callbacks -> (source, amount) -> {
		for (Hurt callback : callbacks) {
			float newAmount = callback.onHurt(source, amount);
			if (newAmount != amount) return newAmount;
		}
		return amount;
	});

	/**
	 *Same as forge's LivingHurtEvent will be renamed to HURT in 1.20
	 */
	public static final Event<ActuallyHurt> ACTUALLY_HURT = EventFactory.createArrayBacked(ActuallyHurt.class, callbacks -> (source, damaged, amount) -> {
		for (ActuallyHurt callback : callbacks) {
			float newAmount = callback.onHurt(source, damaged, amount);
			if (newAmount != amount) return newAmount;
		}
		return amount;
	});

	// TOOD: Fully implement with asm
	public static final Event<CheckSpawn> CHECK_SPAWN = EventFactory.createArrayBacked(CheckSpawn.class, callbacks -> ((entity, world, x, y, z, spawner, spawnReason) -> {
		for (CheckSpawn callback : callbacks)
			if (!callback.onCheckSpawn(entity, world, x, y, z, spawner, spawnReason))
				return true;
		return false;
	}));

	public static final Event<Jump> JUMP = EventFactory.createArrayBacked(Jump.class, callbacks -> (entity) -> {
		for (Jump callback : callbacks) {
			callback.onLivingEntityJump(entity);
		}
	});

	public static final Event<Attack> ATTACK = EventFactory.createArrayBacked(Attack.class, callbacks -> (entity, source, amount) -> {
		for (Attack callback : callbacks) {
			if (callback.onAttack(entity, source, amount)) {
				return true;
			}
		}
		return false;
	});

	public static final Event<EquipmentChange> EQUIPMENT_CHANGE = EventFactory.createArrayBacked(EquipmentChange.class, callbacks -> ((entity, slot, from, to) -> {
		for (EquipmentChange callback : callbacks)
			callback.onEquipmentChange(entity, slot, from, to);
	}));

	public static final Event<Visibility> VISIBILITY = EventFactory.createArrayBacked(Visibility.class, callbacks -> (entity, lookingEntity, originalMultiplier) -> {
		for (Visibility e : callbacks) {
			double newMultiplier = e.getEntityVisibilityMultiplier(entity, lookingEntity, originalMultiplier);
			if (newMultiplier != originalMultiplier)
				return newMultiplier;
		}
		return originalMultiplier;
	});

	public static final Event<ChangeTarget> CHANGE_TARGET = EventFactory.createArrayBacked(ChangeTarget.class, callbacks -> (event) -> {
		for (ChangeTarget e : callbacks) {
			e.onChangeTarget(event);
			if (event.isCanceled())
				return;
		}
	});

	@FunctionalInterface
	public interface EquipmentChange {
		void onEquipmentChange(LivingEntity entity, EquipmentSlot slot, @NotNull ItemStack from, @NotNull ItemStack to);
	}

	@FunctionalInterface
	public interface Attack {
		boolean onAttack(LivingEntity entity, DamageSource source, float amount);
	}

	@FunctionalInterface
	public interface Hurt {
		float onHurt(DamageSource source, float amount);
	}

	@FunctionalInterface
	public interface ActuallyHurt {
		float onHurt(DamageSource source, LivingEntity damaged, float amount);
	}

	@FunctionalInterface
	public interface CheckSpawn {
		boolean onCheckSpawn(Mob entity, LevelAccessor world, double x, double y, double z, @Nullable BaseSpawner spawner, MobSpawnType spawnReason);
	}

	@FunctionalInterface
	public interface Fall {
		void onFall(FallEvent event);

		final class FallEvent extends EntityEvent {
			private final DamageSource source;
			private float distance, damageMultiplier;

			public FallEvent(LivingEntity entity, DamageSource source, float distance, float damageMultiplier) {
				super(entity);
				this.source = source;
				this.distance = distance;
				this.damageMultiplier = damageMultiplier;
			}

			@Override
			public void sendEvent() {
				FALL.invoker().onFall(this);
			}

			public DamageSource getSource() { return source; }
			public float getDistance() { return distance; }
			public float getDamageMultiplier() { return damageMultiplier; }
			public void setDamageMultiplier(float damageMultiplier) { this.damageMultiplier = damageMultiplier; }
			public void setDistance(float distance) { this.distance = distance; }
		}
	}

	@FunctionalInterface
	public interface ExperienceDrop {
		int onLivingEntityExperienceDrop(int i, Player player);
	}

	@FunctionalInterface
	public interface ExperienceDropNew {
		int onLivingEntityExperienceDrop(int i, Player attackingPlayer, LivingEntity entity);
	}

	@FunctionalInterface
	public interface KnockBackStrength {
		double onLivingEntityTakeKnockback(double strength, Player player);
	}

	@FunctionalInterface
	public interface Drops {
		boolean onLivingEntityDrops(LivingEntity target, DamageSource source, Collection<ItemEntity> drops);
	}

	@FunctionalInterface
	public interface LootingLevel {
		int modifyLootingLevel(DamageSource source, LivingEntity target, int currentLevel, boolean recentlyHit);
	}

	@FunctionalInterface
	public interface Tick {
		void onLivingEntityTick(LivingEntity entity);
	}

	@FunctionalInterface
	public interface Jump {
		void onLivingEntityJump(LivingEntity entity);
	}

	@FunctionalInterface
	public interface Visibility {
		double getEntityVisibilityMultiplier(LivingEntity entity, Entity lookingEntity, double originalMultiplier);
	}

	@FunctionalInterface
	public interface ChangeTarget {
		void onChangeTarget(ChangeTargetEvent event);

		final class ChangeTargetEvent extends EntityEvent {
			private final ILivingTargetType targetType;
			private final LivingEntity originalTarget;
			private LivingEntity newTarget;

			public ChangeTargetEvent(LivingEntity entity, LivingEntity originalTarget, ILivingTargetType targetType) {
				super(entity);
				this.originalTarget = originalTarget;
				this.newTarget = originalTarget;
				this.targetType = targetType;
			}

			@Override
			public void sendEvent() {
				CHANGE_TARGET.invoker().onChangeTarget(this);
			}

			public LivingEntity getNewTarget() { return newTarget; }
			public void setNewTarget(LivingEntity newTarget) { this.newTarget = newTarget; }
			public ILivingTargetType getTargetType() { return targetType; }
			public LivingEntity getOriginalTarget() { return originalTarget; }

			public interface ILivingTargetType { }

			/**
			 * This enum contains two default living target types.
			 */
			public enum LivingTargetType implements ILivingTargetType {
				/**
				 * This target type indicates that the target has been set by calling {@link Mob#setTarget(LivingEntity)}.
				 */
				MOB_TARGET,
				/**
				 * This target type indicates that the target has been set by the {@link StartAttacking} behavior.
				 */
				BEHAVIOR_TARGET;
			}
		}
	}
}
