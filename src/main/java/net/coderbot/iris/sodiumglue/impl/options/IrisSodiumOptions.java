package net.coderbot.iris.sodiumglue.impl.options;

import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.client.options.GameOptions;

public class IrisSodiumOptions {
    public static OptionImpl<GameOptions, SupportedGraphicsMode> createLimitedVideoSettingsButton(MinecraftOptionsStorage vanillaOpts) {
        return OptionImpl.createBuilder(SupportedGraphicsMode.class, vanillaOpts)
                .setName("Graphics Quality")
                .setTooltip("The default graphics quality controls some legacy options and is necessary for mod compatibility. If the options below are left to " +
                        "\"Default\", they will use this setting. Fabulous graphics are blocked while shaders are enabled.")
                .setControl(option -> new CyclingControl<>(option, SupportedGraphicsMode.class, new String[] { "Fast", "Fancy" }))
                .setBinding(
                        (opts, value) -> opts.graphicsMode = value.toVanilla(),
                        opts -> SupportedGraphicsMode.fromVanilla(opts.graphicsMode))
                .setImpact(OptionImpact.HIGH)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build();
    }
}
