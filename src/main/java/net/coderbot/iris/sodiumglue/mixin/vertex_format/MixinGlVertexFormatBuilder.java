package net.coderbot.iris.sodiumglue.mixin.vertex_format;

import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttribute;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.render.chunk.format.ChunkMeshAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.EnumMap;

@Mixin(GlVertexFormat.Builder.class)
public class MixinGlVertexFormatBuilder {
    private static final GlVertexAttribute EMPTY
            = new GlVertexAttribute(GlVertexAttributeFormat.FLOAT, 0, false, 0, 0);

    @Redirect(method = "build",
            at = @At(value = "INVOKE",
                    target = "java/util/EnumMap.get (Ljava/lang/Object;)Ljava/lang/Object;"),
            remap = false)
    private Object iris$suppressMissingAttributes(EnumMap<?, ?> map, Object key) {
        Object value = map.get(key);

        if (value == null) {
            if (key == ChunkMeshAttribute.NORMAL || key == ChunkMeshAttribute.TANGENT
                    || key == ChunkMeshAttribute.MID_TEX_COORD || key == ChunkMeshAttribute.BLOCK_ID) {
                // Missing these attributes is acceptable and will be handled properly.
                return EMPTY;
            }
        }

        return value;
    }
}
