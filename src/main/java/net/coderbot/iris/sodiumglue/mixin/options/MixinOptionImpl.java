package net.coderbot.iris.sodiumglue.mixin.options;

import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import net.coderbot.iris.sodiumglue.impl.options.OptionImplExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

/**
 * Allows a slider to be dynamically enabled or disabled based on some external condition.
 */
@Mixin(OptionImpl.class)
public class MixinOptionImpl implements OptionImplExtended  {
    @Unique
    private BooleanSupplier iris$dynamicallyEnabled;

    @Override
    public void iris$dynamicallyEnable(BooleanSupplier enabled) {
        this.iris$dynamicallyEnabled = enabled;
    }

    @Inject(method = "isAvailable()V", at = @At("HEAD"), cancellable = true, remap = false)
    private void iris$dynamicallyEnable(CallbackInfoReturnable<Boolean> cir) {
        if (iris$dynamicallyEnabled != null) {
            cir.setReturnValue(iris$dynamicallyEnabled.getAsBoolean());
        }
    }
}
