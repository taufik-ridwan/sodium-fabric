package net.coderbot.iris.sodiumglue.duck;

import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import net.minecraft.client.util.math.MatrixStack;

public interface ChunkRenderBackendExt {
    void begin(MatrixStack matrixStack, BlockRenderPass pass);
}
