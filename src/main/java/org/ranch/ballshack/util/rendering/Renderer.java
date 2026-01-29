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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import org.ranch.ballshack.BallsHack;

import java.awt.*;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class Renderer {

	private final MinecraftClient mc;

	private BufferBuilder buffer;
	private final BufferAllocator allocator = new BufferAllocator(RenderLayer.DEFAULT_BUFFER_SIZE);

	private static Vector4f colorModulator = new Vector4f(1f, 1f, 1f, 1f);
	MappableRingBuffer vertexBuffer;

	private static Renderer instance;

	public static Renderer getInstance() {
		if (instance == null) {
			instance = new Renderer(BallsHack.mc);
		}
		return instance;
	}

	private Renderer(MinecraftClient mc) {
		this.mc = mc;
	}

	public void setColorModulator(float r, float g, float b, float a) {
		colorModulator = new Vector4f(r, g, b, a);
	}

	public void renderCube(Box box, Color color, MatrixStack matrixStack) {
		setupRender(matrixStack, BallsRenderPipelines.QUADS);

		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;

		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;
		float a = color.getAlpha() / 255.0f;

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();

		buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
		buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
		buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
		buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a);

		buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
		buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
		buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
		buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a);

		buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a);
		buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
		buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
		buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a);

		buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
		buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
		buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
		buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);

		buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
		buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
		buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
		buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a);

		buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
		buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
		buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
		buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a);

		matrixStack.pop();
	}

	public void renderCubeOutlines(Box box, float width, Color color, MatrixStack matrixStack) {

		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;

		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;
		float a = color.getAlpha() / 255.0f;

		//lower square
		renderLine(new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), width, color, matrixStack);

		renderLine(new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2), width, color, matrixStack);

		renderLine(new Vec3d(x2, y1, z2), new Vec3d(x1, y1, z2), width, color, matrixStack);

		renderLine(new Vec3d(x1, y1, z2), new Vec3d(x1, y1, z1), width, color, matrixStack);

		//vertical lines
		renderLine(new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), width, color, matrixStack);

		renderLine(new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), width, color, matrixStack);

		renderLine(new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), width, color, matrixStack);

		renderLine(new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), width, color, matrixStack);

		//upper square
		renderLine(new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), width, color, matrixStack);

		renderLine(new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), width, color, matrixStack);

		renderLine(new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), width, color, matrixStack);

		renderLine(new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), width, color, matrixStack);
	}

	public void renderLine(Vec3d start, Vec3d end, float width, Color color, MatrixStack matrixStack) {
		if (mc.player == null)
			return;

		setupRender(matrixStack, BallsRenderPipelines.QUADS);

		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;
		float a = color.getAlpha() / 255.0f;

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();

		Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

		double startDistance = cameraPos.distanceTo(start);
		double endDistance = cameraPos.distanceTo(end);

		boolean bl = mc.options.getPerspective().isFirstPerson();
		float f = mc.options.getFovEffectScale().getValue().floatValue();
		float fovMultiplier = (float) MathHelper.clamp(mc.player.getFovMultiplier(bl, f), 0.1f, 1.5);

		float fov = (float) mc.options.getFov().getValue() * fovMultiplier;
		float screenHeight = (float) mc.getWindow().getHeight();

		double v = Math.tan(Math.toRadians(fov) / 2.0);
		double startWorldWidth = width * (startDistance * 2.0 * v / screenHeight);
		double endWorldWidth = width * (endDistance * 2.0 * v / screenHeight);

		Vec3d lineDir = end.subtract(start).normalize();
		Vec3d toCam = cameraPos.subtract(start).normalize();

		Vec3d perpendicularDir = lineDir.crossProduct(toCam).normalize();

		Vec3d perpendicularStart = perpendicularDir.multiply(startWorldWidth / 2.0);

		Vec3d perpendicularEnd = perpendicularDir.multiply(endWorldWidth / 2.0);

		Vec3d v1 = start.add(perpendicularStart);
		Vec3d v2 = start.subtract(perpendicularStart);

		Vec3d v3 = end.subtract(perpendicularEnd);
		Vec3d v4 = end.add(perpendicularEnd);

		// V1 and V2 define the near edge, V3 and V4 define the far edge
		buffer.vertex(matrix, (float) v1.x, (float) v1.y, (float) v1.z).color(r, g, b, a);
		buffer.vertex(matrix, (float) v2.x, (float) v2.y, (float) v2.z).color(r, g, b, a);
		buffer.vertex(matrix, (float) v3.x, (float) v3.y, (float) v3.z).color(r, g, b, a);
		buffer.vertex(matrix, (float) v4.x, (float) v4.y, (float) v4.z).color(r, g, b, a);

		matrixStack.pop();
	}

	public void renderArrow(Vec3d start, Vec3d end, float width, float tipSize /* AYYO SUSS LMAOOO DANK*/, Color color, MatrixStack matrixStack) {
		Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
		Vec3d toCam = cameraPos.subtract(start).normalize();
		Vec3d dir = end.subtract(start);
		Vec3d perp = dir.crossProduct(toCam).normalize().multiply(tipSize);

		renderLine(start, end, width, color, matrixStack);
		renderLine(end, perp.add(end.subtract(dir.normalize().multiply(tipSize))), width, color, matrixStack); // good luck understading this future me
		renderLine(end, perp.negate().add(end.subtract(dir.normalize().multiply(tipSize))), width, color, matrixStack);
	}


	public void renderCustom(GeometryWriter geometryWriter, MatrixStack matrixStack, RenderPipeline pipeline) {
		setupRender(matrixStack, pipeline);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();

		geometryWriter.write(buffer, matrix);

		matrixStack.pop();
	}

	private void setupRender(MatrixStack matrixStack, RenderPipeline pipeline) {
		matrixStack.push();
		Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
		matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		if (buffer == null) {
			buffer = new BufferBuilder(allocator, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
		}
	}

	public void draw(RenderPipeline renderPipeline) {
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
					.write(RenderSystem.getModelViewMatrix(), colorModulator, new Vector3f(), RenderSystem.getTextureMatrix(), 1f);
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
