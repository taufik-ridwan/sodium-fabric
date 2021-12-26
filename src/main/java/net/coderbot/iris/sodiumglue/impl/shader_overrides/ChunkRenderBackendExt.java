package net.coderbot.iris.sodiumglue.impl.shader_overrides;

import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import net.minecraft.client.util.math.MatrixStack;

public interface ChunkRenderBackendExt {
    void iris$begin(MatrixStack matrixStack, BlockRenderPass pass);
}
