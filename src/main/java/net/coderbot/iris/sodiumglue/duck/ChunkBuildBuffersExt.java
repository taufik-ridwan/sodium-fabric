package net.coderbot.iris.sodiumglue.duck;

import net.minecraft.block.BlockState;

public interface ChunkBuildBuffersExt {
    void iris$setMaterialId(BlockState state, short renderType);
    void iris$resetMaterialId();
}
