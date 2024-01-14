package io.github.fabricators_of_create.porting_lib.util;

import java.util.Arrays;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtil {
	/**
	 * @deprecated {@link ItemStack#matches(ItemStack, ItemStack)}
	 */
	@Deprecated(forRemoval = true)
	public static boolean equals(ItemStack stack1, ItemStack stack2, boolean limitTags) {
		if (stack1.isEmpty()) {
			return stack2.isEmpty();
		} else {
			return !stack2.isEmpty() && stack1.getCount() == stack2.getCount() && stack1.getItem() == stack2.getItem() &&
					(limitTags ? areTagsEqual(stack1, stack2) : ItemStack.isSameItemSameTags(stack1, stack2));
		}
	}

	public static boolean areTagsEqual(ItemStack stack1, ItemStack stack2) {
		CompoundTag tag1 = stack1.getTag();
		CompoundTag tag2 = stack2.getTag();
		if (tag1 == null) {
			return tag2 == null;
		} else {
			return tag1.equals(tag2);
		}
	}

	public static ItemStack[] createEmptyStackArray(int size) {
		ItemStack[] stacks = new ItemStack[size];
		Arrays.fill(stacks, ItemStack.EMPTY);
		return stacks;
	}

	/**
	 * @deprecated {@link ItemStack#isSameItemSameTags(ItemStack, ItemStack)}
	 */
	@Deprecated(forRemoval = true)
	public static boolean canItemStacksStack(ItemStack first, ItemStack second) {
		if (first.isEmpty() || !ItemStack.isSameItem(first, second) || first.hasTag() != second.hasTag()) return false;

		return !first.hasTag() || first.getTag().equals(second.getTag());
	}
}
