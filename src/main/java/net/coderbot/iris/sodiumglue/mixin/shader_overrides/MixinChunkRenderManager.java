package net.coderbot.iris.sodiumglue.mixin.shader_overrides;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderBackend;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderManager;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import net.coderbot.iris.sodiumglue.duck.ChunkRenderBackendExt;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkRenderManager.class)
public class MixinChunkRenderManager {
    @Redirect(method = "renderLayer",
            at = @At(value = "INVOKE",
                    target = "me/jellysquid/mods/sodium/client/render/chunk/ChunkRenderBackend.begin (" +
                                "Lnet/minecraft/client/util/math/MatrixStack;" +
                            ")V"))
    private void iris$backendBeginExt(ChunkRenderBackend<?> backend, MatrixStack matrixStack,
                                      MatrixStack matrixStackArg, BlockRenderPass pass, double x, double y, double z) {
        if (backend instanceof ChunkRenderBackendExt) {
            ((ChunkRenderBackendExt) backend).iris$begin(matrixStack, pass);
        } else {
            backend.begin(matrixStack);
        }
    }
}
