package net.coderbot.iris.sodiumglue.mixin.options;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import net.coderbot.iris.gui.screen.ShaderPackScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Adds our Shader Packs button to the Sodium options GUI.
 */
@Mixin(SodiumOptionsGUI.class)
public class MixinSodiumOptionsGUI extends Screen {
    @Shadow(remap = false)
    @Final
    private List<OptionPage> pages;

    @Unique
    private OptionPage shaderPacks;

    // make compiler happy
    protected MixinSodiumOptionsGUI(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void iris$onInit(Screen prevScreen, CallbackInfo ci) {
        String shaderPacksTranslated = new TranslatableText("options.iris.shaderPackSelection").getString();
        shaderPacks = new OptionPage(shaderPacksTranslated, ImmutableList.of());
        pages.add(shaderPacks);
    }

    @Inject(method = "setPage", at = @At("HEAD"), remap = false, cancellable = true)
    private void iris$onSetPage(OptionPage page, CallbackInfo ci) {
        if (page == shaderPacks) {
            client.openScreen(new ShaderPackScreen(this));
            ci.cancel();
        }
    }
}
