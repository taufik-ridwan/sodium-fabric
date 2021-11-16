package net.coderbot.iris.sodiumglue.impl.block_id;

import net.minecraft.block.BlockState;

public interface ChunkBuildBuffersExt {
    void iris$setMaterialId(BlockState state, short renderType);
    void iris$resetMaterialId();
}
