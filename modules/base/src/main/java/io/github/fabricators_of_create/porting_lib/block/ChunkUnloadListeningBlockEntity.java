package io.github.fabricators_of_create.porting_lib.block;

public interface ChunkUnloadListeningBlockEntity {
	default void onChunkUnloaded() {}
}
