package net.coderbot.iris.sodiumglue.mixin.separate_ao;

import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuffers;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import me.jellysquid.mods.sodium.client.util.color.ColorABGR;
import net.coderbot.iris.block_rendering.BlockRenderingSettings;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Basically the same as {@link MixinBlockRenderer}, but for fluid rendering.
 */
@Mixin(FluidRenderer.class)
public class MixinFluidRenderer {
    @Unique
    private boolean useSeparateAo;

    @Inject(method = "render", remap = false, at = @At("HEAD"))
    private void iris$cacheSeparateAoSetting(BlockRenderView world, FluidState fluidState, BlockPos pos,
                                             ChunkModelBuffers buffers, CallbackInfoReturnable<Boolean> cir) {
        this.useSeparateAo = BlockRenderingSettings.INSTANCE.shouldUseSeparateAo();
    }

    @Redirect(method = "calculateQuadColors", remap = false,
            at = @At(value = "INVOKE", target = "me/jellysquid/mods/sodium/client/util/color/ColorABGR.mul (IF)I", remap = false))
    private int iris$applySeparateAo(int color, float ao) {
        if (useSeparateAo) {
            color &= 0x00FFFFFF;
            color |= ((int) (ao * 255.0f)) << 24;
        } else {
            color = ColorABGR.mul(color, ao);
        }

        return color;
    }
}
