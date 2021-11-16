package net.coderbot.iris.sodiumglue.mixin.directional_shading;

import me.jellysquid.mods.sodium.client.model.light.flat.FlatLightPipeline;
import net.coderbot.iris.block_rendering.BlockRenderingSettings;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlatLightPipeline.class)
public class MixinFlatLightPipeline {
    @Redirect(method = "calculate", at = @At(value = "INVOKE",
            target = "net/minecraft/world/BlockRenderView.getBrightness (Lnet/minecraft/util/math/Direction;Z)F"))
    private float iris$getBrightness(BlockRenderView world, Direction direction, boolean shaded) {
        if (BlockRenderingSettings.INSTANCE.shouldDisableDirectionalShading()) {
            return 1.0F;
        } else {
            return world.getBrightness(direction, shaded);
        }
    }
}
