package io.github.fabricators_of_create.porting_lib.extensions;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IShearable {
	default boolean isShearable(@NotNull ItemStack item, Level world, BlockPos pos) {
		return false;
	}

	@NotNull
	default List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
		return Collections.emptyList();
	}
}
