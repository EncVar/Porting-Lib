package io.github.fabricators_of_create.porting_lib.loot;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import org.jetbrains.annotations.NotNull;


//From forge
public abstract class LootModifier implements IGlobalLootModifier {
	protected final LootItemCondition[] conditions;
	private final Predicate<LootContext> combinedConditions;

	/**
	 * Constructs a LootModifier.
	 *
	 * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
	 */
	protected LootModifier(LootItemCondition[] conditionsIn) {
		this.conditions = conditionsIn;
		this.combinedConditions = LootItemConditions.andConditions(conditionsIn);
	}

	@NotNull
	@Override
	public final List<ItemStack> apply(List<ItemStack> generatedLoot, LootContext context) {
		return this.combinedConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
	}

	/**
	 * Applies the modifier to the generated loot (all loot conditions have already been checked
	 * and have returned true).
	 *
	 * @param generatedLoot the list of ItemStacks that will be dropped, generated by loot tables
	 * @param context       the LootContext, identical to what is passed to loot tables
	 * @return modified loot drops
	 */
	@NotNull
	protected abstract List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context);
}
