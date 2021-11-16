package me.jellysquid.mods.sodium.client.render.chunk.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.gl.shader.*;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.compat.LegacyFogHelper;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderBackend;
import me.jellysquid.mods.sodium.client.render.chunk.format.ChunkMeshAttribute;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;

import net.coderbot.iris.sodiumglue.backend.IrisChunkProgramOverrides;
import net.coderbot.iris.gl.program.ProgramUniforms;
import net.coderbot.iris.shadows.ShadowRenderingState;
import net.coderbot.iris.sodiumglue.duck.ChunkRenderBackendExt;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.EnumMap;

public abstract class ChunkRenderShaderBackend<T extends ChunkGraphicsState>
        implements ChunkRenderBackend<T>, ChunkRenderBackendExt {
    // Iris start
    private final IrisChunkProgramOverrides irisChunkProgramOverrides = new IrisChunkProgramOverrides();
    private RenderDevice device;
    // Iris end
    private final EnumMap<ChunkFogMode, ChunkProgram> programs = new EnumMap<>(ChunkFogMode.class);

    protected final ChunkVertexType vertexType;
    protected final GlVertexFormat<ChunkMeshAttribute> vertexFormat;

    protected ChunkProgram activeProgram;

    public ChunkRenderShaderBackend(ChunkVertexType vertexType) {
        this.vertexType = vertexType;
        this.vertexFormat = vertexType.getCustomVertexFormat();
    }

    private ChunkProgram createShader(RenderDevice device, ChunkFogMode fogMode, GlVertexFormat<ChunkMeshAttribute> vertexFormat) {
        GlShader vertShader = ShaderLoader.loadShader(device, ShaderType.VERTEX,
                new Identifier("sodium", "chunk_gl20.v.glsl"), fogMode.getDefines());

        GlShader fragShader = ShaderLoader.loadShader(device, ShaderType.FRAGMENT,
                new Identifier("sodium", "chunk_gl20.f.glsl"), fogMode.getDefines());

        try {
            return GlProgram.builder(new Identifier("sodium", "chunk_shader"))
                    .attachShader(vertShader)
                    .attachShader(fragShader)
                    .bindAttribute("a_Pos", ChunkShaderBindingPoints.POSITION)
                    .bindAttribute("a_Color", ChunkShaderBindingPoints.COLOR)
                    .bindAttribute("a_TexCoord", ChunkShaderBindingPoints.TEX_COORD)
                    .bindAttribute("a_LightCoord", ChunkShaderBindingPoints.LIGHT_COORD)
                    .bindAttribute("d_ModelOffset", ChunkShaderBindingPoints.MODEL_OFFSET)
                    .build((program, name) -> new ChunkProgram(device, program, name, fogMode.getFactory()));
        } finally {
            vertShader.delete();
            fragShader.delete();
        }
    }

    @Override
    public final void createShaders(RenderDevice device) {
        // Iris start
        this.device = device;
        irisChunkProgramOverrides.createShaders(device);
        // Iris end
        this.programs.put(ChunkFogMode.NONE, this.createShader(device, ChunkFogMode.NONE, this.vertexFormat));
        this.programs.put(ChunkFogMode.LINEAR, this.createShader(device, ChunkFogMode.LINEAR, this.vertexFormat));
        this.programs.put(ChunkFogMode.EXP2, this.createShader(device, ChunkFogMode.EXP2, this.vertexFormat));
    }

    @Override
    public void begin(MatrixStack matrixStack) {
        throw new UnsupportedOperationException("Attempted to call non-pass-sensitive begin() method");
    }

    @Override
    public void begin(MatrixStack matrixStack, BlockRenderPass pass) {
        this.activeProgram = this.programs.get(LegacyFogHelper.getFogMode());
        // Iris start
        {
            if (ShadowRenderingState.areShadowsCurrentlyBeingRendered()) {
                // No back face culling during the shadow pass
                // TODO: Hopefully this won't be necessary in the future...
                RenderSystem.disableCull();
            }

            ChunkProgram override = irisChunkProgramOverrides.getProgramOverride(device, pass);

            if (override != null) {
                this.activeProgram = override;
            }
        }
        // Iris end
        this.activeProgram.bind();
        this.activeProgram.setup(matrixStack, this.vertexType.getModelScale(), this.vertexType.getTextureScale());
    }

    @Override
    public void end(MatrixStack matrixStack) {
        this.activeProgram.unbind();
        this.activeProgram = null;
        // Iris start
        ProgramUniforms.clearActiveUniforms();
        // Iris end
    }

    @Override
    public void delete() {
        // Iris start
        irisChunkProgramOverrides.deleteShaders();
        // Iris end
        for (ChunkProgram shader : this.programs.values()) {
            shader.delete();
        }
    }

    @Override
    public ChunkVertexType getVertexType() {
        return this.vertexType;
    }
}
