package net.coderbot.iris.sodiumglue.mixin.block_id;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import net.coderbot.iris.sodiumglue.duck.ChunkBuildBuffersExt;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Passes material ID information indirectly to the vertex writer to support the mc_Entity part of the vertex format.
 */
@Mixin(ChunkRenderRebuildTask.class)
public class MixinChunkRenderRebuildTask {
    @Redirect(method = "performBuild", remap = false, at = @At(value = "INVOKE",
            target = "net/minecraft/client/render/RenderLayers.getBlockLayer (" +
                        "Lnet/minecraft/block/BlockState;" +
                    ")Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer iris$wrapGetBlockLayer(BlockState blockState, ChunkRenderCacheLocal cache,
                                               ChunkBuildBuffers buffers, CancellationSource cancellationSource) {
        if (buffers instanceof ChunkBuildBuffersExt) {
            ((ChunkBuildBuffersExt) buffers).iris$setMaterialId(blockState, (short) -1);
        }

        return RenderLayers.getBlockLayer(blockState);
    }

    @Redirect(method = "performBuild", remap = false, at = @At(value = "INVOKE",
            target = "net/minecraft/client/render/RenderLayers.getFluidLayer (" +
                        "Lnet/minecraft/fluid/FluidState;" +
                    ")Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer iris$wrapGetFluidLayer(FluidState fluidState, ChunkRenderCacheLocal cache,
                                               ChunkBuildBuffers buffers, CancellationSource cancellationSource) {
        if (buffers instanceof ChunkBuildBuffersExt) {
            // All fluids have a ShadersMod render type of 1, to match behavior of Minecraft 1.7 and earlier.
            ((ChunkBuildBuffersExt) buffers).iris$setMaterialId(fluidState.getBlockState(), (short) 1);
        }

        return RenderLayers.getFluidLayer(fluidState);
    }

    @Inject(method = "performBuild", remap = false,
            at = @At(value = "INVOKE", target = "net/minecraft/block/Block.hasBlockEntity ()Z"))
    private void iris$resetId(ChunkRenderCacheLocal cache, ChunkBuildBuffers buffers,
                              CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult<?>> cir) {
        if (buffers instanceof ChunkBuildBuffersExt) {
            ((ChunkBuildBuffersExt) buffers).iris$resetMaterialId();
        }
    }
}
