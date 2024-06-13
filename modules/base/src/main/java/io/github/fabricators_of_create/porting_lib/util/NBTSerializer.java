package io.github.fabricators_of_create.porting_lib.util;

import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * Use deserializeNBT/serializeNBT directly on whatever you need it on as it is now correctly interface injected
 */
@Deprecated(forRemoval = true, since = "June 12th 2024")
public class NBTSerializer {
	@Deprecated(forRemoval = true, since = "June 12th 2024")
	public static void deserializeNBT(Object o, Tag nbt) {
		((INBTSerializable) o).deserializeNBT(nbt);
	}

	@Deprecated(forRemoval = true, since = "June 12th 2024")
	public static Tag serializeNBT(Object o) {
		return ((INBTSerializable) o).serializeNBT();
	}

	@Deprecated(forRemoval = true, since = "June 12th 2024")
	public static CompoundTag serializeNBTCompound(Object o) {
		Tag tag = ((INBTSerializable) o).serializeNBT();
		if (tag instanceof CompoundTag c)
			return c;
		throw new RuntimeException("Cannot use serializeNBTCompound with a type that does not return a CompoundTag");
	}
}
