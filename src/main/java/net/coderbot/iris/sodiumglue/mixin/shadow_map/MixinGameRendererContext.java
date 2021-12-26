package net.coderbot.iris.sodiumglue.mixin.shadow_map;

import me.jellysquid.mods.sodium.client.render.GameRendererContext;
import net.coderbot.iris.shadows.ShadowRenderingState;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Allows the Iris shadow map projection matrix to be used during shadow rendering instead of the player view's
 * projection matrix.
 */
@Mixin(GameRendererContext.class)
public class MixinGameRendererContext {
    @Redirect(method = "getModelViewProjectionMatrix",
            at = @At(value = "INVOKE",
                    target = "net/minecraft/util/math/Matrix4f.copy ()Lnet/minecraft/util/math/Matrix4f;"))
    private static Matrix4f iris$useShadowProjectionMatrix(Matrix4f matrix) {
        if (ShadowRenderingState.areShadowsCurrentlyBeingRendered()) {
            return ShadowRenderingState.getShadowOrthoMatrix();
        } else {
            return matrix.copy();
        }
    }
}
