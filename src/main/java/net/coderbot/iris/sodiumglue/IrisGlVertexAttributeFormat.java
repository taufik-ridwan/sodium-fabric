package net.coderbot.iris.sodiumglue;

import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import org.lwjgl.opengl.GL20C;

public class IrisGlVertexAttributeFormat {
    public static final GlVertexAttributeFormat BYTE = new GlVertexAttributeFormat(GL20C.GL_BYTE, 1);
}
