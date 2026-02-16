package org.ranch.ballshack.util.rendering;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import org.ranch.ballshack.AiSlop;
import org.ranch.ballshack.BallsHack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class Renderer {

	private final MinecraftClient mc;

	private BufferBuilder buffer;
	private final BufferAllocator allocator = new BufferAllocator(RenderLayer.field_64008); // uhh idk

	private static Vector4f colorModulator = new Vector4f(1f, 1f, 1f, 1f);
	MappableRingBuffer vertexBuffer;

	private static Renderer instance;

	private final List<QuadRenderCommand> quadRenderCommandList;
	private final List<LineRenderCommand> lineRenderCommandList;

	public record QuadRenderCommand(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, Color color, MatrixStack matrixStack) {}
	public record LineRenderCommand(Vec3d start, Vec3d end, Color color, MatrixStack matrixStack) {}

	public static Renderer getInstance() {
		if (instance == null) {
			instance = new Renderer(BallsHack.mc);
		}
		return instance;
	}

	private Renderer(MinecraftClient mc) {
		this.mc = mc;
		quadRenderCommandList = new ArrayList<>();
		lineRenderCommandList = new ArrayList<>();
	}

	public void setColorModulator(float r, float g, float b, float a) {
		colorModulator = new Vector4f(r, g, b, a);
	}

	/* Quad queuing */
	public void queueQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, Color color, MatrixStack matrixStack) {
		quadRenderCommandList.add(
				new QuadRenderCommand(
						v1,
						v2,
						v3,
						v4,
						color,
						matrixStack
				)
		);
	}

	public void queueCube(Box box, Color color, MatrixStack matrixStack) {
		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;

		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		queueQuad(
				new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y1, z1),
				color, matrixStack
		);

		queueQuad(
				new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), new Vec3d(x2, y1, z2),
				color, matrixStack
		);

		queueQuad(
				new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), new Vec3d(x1, y1, z2),
				color, matrixStack
		);

		queueQuad(
				new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), new Vec3d(x1, y1, z1),
				color, matrixStack
		);

		queueQuad(
				new Vec3d(x1, y2, z1), new Vec3d(x1, y2, z2), new Vec3d(x2, y2, z2), new Vec3d(x2, y2, z1),
				color, matrixStack
		);

		queueQuad(
				new Vec3d(x1, y1, z2), new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2),
				color, matrixStack
		);
	}

	/* Line queuing */
	public void queueLine(Vec3d start, Vec3d end, Color color, MatrixStack matrixStack) {
		lineRenderCommandList.add(
				new LineRenderCommand(
						start,
						end,
						color,
						matrixStack
				)
		);
	}

	public void queueTracer(Vec3d end, Color color, MatrixStack matrixStack) {

		Vec3d tracerStart = new Vec3d(0, 0, 100)
			.rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
			.rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
			.add(mc.gameRenderer.getCamera().getCameraPos());

		lineRenderCommandList.add(
				new LineRenderCommand(
						tracerStart,
						end,
						color,
						matrixStack
				)
		);
	}

	public void queueCubeOutline(Box box, Color color, MatrixStack matrixStack) {

		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;

		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		//lower square
		queueLine(new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), color, matrixStack);
		queueLine(new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2), color, matrixStack);
		queueLine(new Vec3d(x2, y1, z2), new Vec3d(x1, y1, z2), color, matrixStack);
		queueLine(new Vec3d(x1, y1, z2), new Vec3d(x1, y1, z1), color, matrixStack);

		//vertical lines
		queueLine(new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), color, matrixStack);
		queueLine(new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), color, matrixStack);
		queueLine(new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), color, matrixStack);
		queueLine(new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), color, matrixStack);

		//upper square
		queueLine(new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), color, matrixStack);
		queueLine(new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), color, matrixStack);
		queueLine(new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), color, matrixStack);
		queueLine(new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), color, matrixStack);
	}

	public void queueArrow(Vec3d start, Vec3d end, float tipSize /* AYYO SUSS LMAOOO DANK*/, Color color, MatrixStack matrixStack) {
		Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();
		Vec3d toCam = cameraPos.subtract(start).normalize();
		Vec3d dir = end.subtract(start);
		Vec3d perp = dir.crossProduct(toCam).normalize().multiply(tipSize);

		queueLine(start, end, color, matrixStack);
		queueLine(end, perp.add(end.subtract(dir.normalize().multiply(tipSize))), color, matrixStack); // good luck understading this future me
		queueLine(end, perp.negate().add(end.subtract(dir.normalize().multiply(tipSize))), color, matrixStack);
	}

	/* Queue rendering */
	public void renderQueues(MatrixStack stack) {
		stack.push();
		Vec3d cameraPos = mc.gameRenderer.getCamera().getCameraPos();
		stack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		// quads
		setBuffer(BallsRenderPipelines.QUADS);
		for (QuadRenderCommand quadCommand : quadRenderCommandList) {
			renderQuad(quadCommand);
		}
		if (!quadRenderCommandList.isEmpty())
			draw(BallsRenderPipelines.QUADS);
		quadRenderCommandList.clear();

		// lines
		setBuffer(BallsRenderPipelines.LINES);
		for (LineRenderCommand lineCommand : lineRenderCommandList) {
			renderLine(lineCommand);
		}
		if (!lineRenderCommandList.isEmpty())
			draw(BallsRenderPipelines.LINES);
		lineRenderCommandList.clear();

		stack.pop();
	}

	private void renderQuad(QuadRenderCommand command) {
		float r = command.color.getRed() / 255.0f;
		float g = command.color.getGreen() / 255.0f;
		float b = command.color.getBlue() / 255.0f;
		float a = command.color.getAlpha() / 255.0f;

		Matrix4f matrix = command.matrixStack.peek().getPositionMatrix();

		buffer.vertex(matrix, (float) command.v1().x, (float) command.v1().y, (float) command.v1().z).color(r, g, b, a);
		buffer.vertex(matrix, (float) command.v2().x, (float) command.v2().y, (float) command.v2().z).color(r, g, b, a);
		buffer.vertex(matrix, (float) command.v3().x, (float) command.v3().y, (float) command.v3().z).color(r, g, b, a);
		buffer.vertex(matrix, (float) command.v4().x, (float) command.v4().y, (float) command.v4().z).color(r, g, b, a);
	}

	@AiSlop
	private void renderLine(LineRenderCommand command) {
		float r = command.color.getRed() / 255.0f;
		float g = command.color.getGreen() / 255.0f;
		float b = command.color.getBlue() / 255.0f;
		float a = command.color.getAlpha() / 255.0f;

		float x1 = (float) command.start.getX();
		float y1 = (float) command.start.getY();
		float z1 = (float) command.start.getZ();

		float x2 = (float) command.end.getX();
		float y2 = (float) command.end.getY();
		float z2 = (float) command.end.getZ();

		Vec3d camPos = mc.gameRenderer.getCamera().getCameraPos();
		float yawRad = (float) Math.toRadians(mc.gameRenderer.getCamera().getYaw());
		float pitchRad = (float) Math.toRadians(mc.gameRenderer.getCamera().getPitch());

		float fx = -(float)(Math.sin(yawRad) * Math.cos(pitchRad));
		float fy = -(float)(Math.sin(pitchRad));
		float fz =  (float)(Math.cos(yawRad) * Math.cos(pitchRad));

		final float NEAR = 0.01f;
		float px = (float)(camPos.x + fx * NEAR);
		float py = (float)(camPos.y + fy * NEAR);
		float pz = (float)(camPos.z + fz * NEAR);

		float s = fx * (x1 - px) + fy * (y1 - py) + fz * (z1 - pz);
		float e = fx * (x2 - px) + fy * (y2 - py) + fz * (z2 - pz);

		/*if (s <= 0.0f && e <= 0.0f) {
			command.matrixStack.pop();
			return;
		}*/

		float ix = 0f, iy = 0f, iz = 0f;
		boolean hasIntersection = false;

		float denom = s - e;
		if (Math.abs(denom) > 1e-6f && (s > 0.0f && e <= 0.0f || s <= 0.0f && e > 0.0f)) {
			float u = s / denom;
			if (u > 0.0f && u < 1.0f) {
				ix = x1 + (x2 - x1) * u;
				iy = y1 + (y2 - y1) * u;
				iz = z1 + (z2 - z1) * u;
				hasIntersection = true;
			}
		}

		float ax = x1, ay = y1, azf = z1;
		float bx = x2, by = y2, bz = z2;

		if (s <= 0.0f && e > 0.0f) {
			ax = ix; ay = iy; azf = iz;
		} else if (s > 0.0f && e <= 0.0f) {
			bx = ix; by = iy; bz = iz;
		}

		Vector3f normal = new Vector3f(bx - ax, by - ay, bz - azf);
		normal.normalize();

		buffer.vertex(command.matrixStack.peek(), ax, ay, azf).color(r, g, b, a).normal(command.matrixStack.peek(), normal);

		if (hasIntersection) {
			buffer.vertex(command.matrixStack.peek(), ix, iy, iz).color(r, g, b, a).normal(command.matrixStack.peek(), normal);
			buffer.vertex(command.matrixStack.peek(), ix, iy, iz).color(r, g, b, a).normal(command.matrixStack.peek(), normal);
		}

		buffer.vertex(command.matrixStack.peek(), bx, by, bz).color(r, g, b, a).normal(command.matrixStack.peek(), normal);
	}

	private void setBuffer(RenderPipeline pipeline) {
		buffer = new BufferBuilder(allocator, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
	}

	@AiSlop
	private void draw(RenderPipeline renderPipeline) {
		if (buffer == null) return;
		try (BuiltBuffer builtBuffer = buffer.end()) {
			BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();
			VertexFormat vertexFormat = drawParameters.format();

			GpuBuffer vertices = upload(drawParameters, vertexFormat, builtBuffer);

			GpuBuffer indices;
			VertexFormat.IndexType indexType;

			if (renderPipeline.getVertexFormatMode() == VertexFormat.DrawMode.QUADS) {
				builtBuffer.sortQuads(allocator, RenderSystem.getProjectionType().getVertexSorter());
				indices = renderPipeline.getVertexFormat().uploadImmediateIndexBuffer(builtBuffer.getSortedBuffer());
				indexType = builtBuffer.getDrawParameters().indexType();
			} else {

				RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(renderPipeline.getVertexFormatMode());
				indices = shapeIndexBuffer.getIndexBuffer(drawParameters.indexCount());
				indexType = shapeIndexBuffer.getIndexType();
			}

			GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
					.write(RenderSystem.getModelViewMatrix(), colorModulator, new Vector3f(), new Matrix4f());
			try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
					.createRenderPass(() -> BallsHack.ID + " renderer", mc.getFramebuffer().getColorAttachmentView(), OptionalInt.empty(), mc.getFramebuffer().getDepthAttachmentView(), OptionalDouble.empty())) {
				renderPass.setPipeline(renderPipeline);

				RenderSystem.bindDefaultUniforms(renderPass);
				renderPass.setUniform("DynamicTransforms", dynamicTransforms);

				renderPass.setVertexBuffer(0, vertices);
				renderPass.setIndexBuffer(indices, indexType);

				renderPass.drawIndexed(0, 0, drawParameters.indexCount(), 1);
			}

			vertexBuffer.rotate();
			buffer = null;
		}
	}

	private GpuBuffer upload(BuiltBuffer.DrawParameters drawParameters, VertexFormat vertexFormat, BuiltBuffer builtBuffer) {

		int buffersize = drawParameters.vertexCount() * vertexFormat.getVertexSize();

		if (vertexBuffer == null || vertexBuffer.size() < buffersize) {
			vertexBuffer = new MappableRingBuffer(() -> BallsHack.ID + " renderer", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE, buffersize);
		}
		GpuBuffer targetBuffer = vertexBuffer.getBlocking();
		CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

		try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(targetBuffer.slice(0, builtBuffer.getBuffer().remaining()), false, true)) {
			MemoryUtil.memCopy(builtBuffer.getBuffer(), mappedView.data());
		}
		return targetBuffer;
	}
}
