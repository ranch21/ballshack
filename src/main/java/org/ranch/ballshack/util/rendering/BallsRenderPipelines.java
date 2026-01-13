package org.ranch.ballshack.util.rendering;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.BallsHack;

public class BallsRenderPipelines {

	public static final RenderPipeline QUADS = RenderPipeline.builder()
			.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
			.withUniform("Projection", UniformType.UNIFORM_BUFFER)
			.withVertexShader("core/position_color")
			.withFragmentShader("core/position_color")
			.withBlend(BlendFunction.TRANSLUCENT)
			.withLocation(Identifier.tryParse(BallsHack.ID, "pipeline/quads"))
			.withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withCull(false)
			.build();
}
