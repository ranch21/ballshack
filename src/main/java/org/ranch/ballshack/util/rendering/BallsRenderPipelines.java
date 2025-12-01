package org.ranch.ballshack.util.rendering;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.BallsHack;

public class BallsRenderPipelines {
	public static final RenderPipeline TRIANGLE_STRIP = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
			.withLocation(Identifier.tryParse(BallsHack.ID, "pipeline/triangle_strip"))
			.withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.build()
	);

	public static final RenderPipeline QUADS = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
			.withLocation(Identifier.tryParse(BallsHack.ID, "pipeline/quads"))
			.withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withCull(false)
			.build()
	);

	public static final RenderPipeline.Snippet RENDERTYPE_BALLS_LINES_SNIPPET = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
			.withVertexShader(Identifier.of(BallsHack.ID, "rendertype_lines"))
			.withFragmentShader(Identifier.of(BallsHack.ID, "rendertype_lines"))
			.withBlend(BlendFunction.TRANSLUCENT)
			.withCull(false)
			.withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINES)
			.buildSnippet();


	public static final RenderPipeline LINES = RenderPipelines.register(RenderPipeline.builder(RENDERTYPE_BALLS_LINES_SNIPPET)
			.withLocation(Identifier.tryParse(BallsHack.ID, "pipeline/line"))
			.withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINES)
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.build()
	);
}
