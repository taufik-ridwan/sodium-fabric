package net.coderbot.iris.sodiumglue.impl.options;

import net.minecraft.client.options.GraphicsMode;

public enum SupportedGraphicsMode {
    FAST, FANCY;

    public static SupportedGraphicsMode fromVanilla(GraphicsMode vanilla) {
        if (vanilla == GraphicsMode.FAST) {
            return FAST;
        } else {
            return FANCY;
        }
    }

    public GraphicsMode toVanilla() {
        if (this == FAST) {
            return GraphicsMode.FAST;
        } else {
            return GraphicsMode.FANCY;
        }
    }
}
