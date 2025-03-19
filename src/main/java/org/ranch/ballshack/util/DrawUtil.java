package org.ranch.ballshack.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;
import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.gui.Colors;

import java.awt.*;

public class DrawUtil {
	public static void drawHorizontalGradient(DrawContext context, int x, int y, int width, int height, Color startColor, Color endColor, int resolution) {
		// forgive me
		width += 1;
		height += 1;
		y -= 1;
		int cX = x;
		int i = 0;
		int blend = 0;
		for (;;) {
			for (int j = 0; j < resolution; j++) {
				context.drawVerticalLine(cX,y,y+height,blendColor(startColor, endColor, (float) blend / width).hashCode());
				cX++;
				i++;
				if (i >= width) return;
			}
			blend += resolution;
		}
	}

	public static void drawVerticalGradient(DrawContext context, int x, int y, int width, int height, Color startColor, Color endColor, int resolution) {
		// forgive me
		width += 1;
		height += 1;
		y -= 1;

		int cY = y;
		int i = 0;
		int blend = 0;
		for (;;) {
			for (int j = 0; j < resolution; j++) {
				context.drawHorizontalLine(x,x + width,cY,blendColor(startColor, endColor, (float) blend / height).hashCode());
				cY++;
				i++;
				if (i >= height) return;
			}
			blend += resolution;
		}
	}

	public static Color blendColor(Color color1, Color color2, float ratio) {

		int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
		int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
		int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);

		return new Color(Math.min(red, 255), Math.min(green, 255), Math.min(blue, 255));
	}

	public static void drawTextRight(DrawContext context, TextRenderer textRend, String text, int x, int y, Color color, boolean shadow) {
		drawText(context, textRend, text, x - textRend.getWidth(text), y, color, shadow);
	}

	public static void drawText(DrawContext context, TextRenderer textRend, String text, int x, int y, Color color, boolean shadow) {

		context.drawText(textRend, text, x, y, color.hashCode(), shadow);

		/*textRend.draw(
				text,
				x,
				y,
				color.hashCode(),
				shadow,
				context.getMatrices().peek().getPositionMatrix(),
				context.get(),
				TextRenderer.TextLayerType.SEE_THROUGH,
				0,
				15728880
		);*/
	}

	public static void drawCube(MatrixStack matrices, Box cube, float r, float g, float b, float a) {

		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		float minX = (float) cube.minX;
		float minY = (float) cube.minY;
		float minZ = (float) cube.minZ;
		float maxX = (float) cube.maxX;
		float maxY = (float) cube.maxY;
		float maxZ = (float) cube.maxZ;

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		bufferBuilder.vertex(matrix, minX, minY, minZ);
		bufferBuilder.vertex(matrix, maxX, minY, minZ);
		bufferBuilder.vertex(matrix, maxX, minY, maxZ);
		bufferBuilder.vertex(matrix, minX, minY, maxZ);

		bufferBuilder.vertex(matrix, minX, maxY, minZ);
		bufferBuilder.vertex(matrix, minX, maxY, maxZ);
		bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
		bufferBuilder.vertex(matrix, maxX, maxY, minZ);

		bufferBuilder.vertex(matrix, minX, minY, minZ);
		bufferBuilder.vertex(matrix, minX, maxY, minZ);
		bufferBuilder.vertex(matrix, maxX, maxY, minZ);
		bufferBuilder.vertex(matrix, maxX, minY, minZ);

		bufferBuilder.vertex(matrix, maxX, minY, minZ);
		bufferBuilder.vertex(matrix, maxX, maxY, minZ);
		bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
		bufferBuilder.vertex(matrix, maxX, minY, maxZ);

		bufferBuilder.vertex(matrix, minX, minY, maxZ);
		bufferBuilder.vertex(matrix, maxX, minY, maxZ);
		bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
		bufferBuilder.vertex(matrix, minX, maxY, maxZ);

		bufferBuilder.vertex(matrix, minX, minY, minZ);
		bufferBuilder.vertex(matrix, minX, minY, maxZ);
		bufferBuilder.vertex(matrix, minX, maxY, maxZ);
		bufferBuilder.vertex(matrix, minX, maxY, minZ);

		RenderSystem.setShader(ShaderProgramKeys.POSITION);
		RenderSystem.setShaderColor(r,g,b,a);

		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

		RenderSystem.setShaderColor(1,1,1,1);
	}

	public static void drawCubeOutline(MatrixStack matrices, Box cube, float r, float g, float b, float a) {

		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		float minX = (float) cube.minX;
		float minY = (float) cube.minY;
		float minZ = (float) cube.minZ;
		float maxX = (float) cube.maxX;
		float maxY = (float) cube.maxY;
		float maxZ = (float) cube.maxZ;

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		bufferBuilder.vertex(matrix, minX, minY, minZ);
		bufferBuilder.vertex(matrix, maxX, minY, minZ);

		bufferBuilder.vertex(matrix, maxX, minY, minZ);
		bufferBuilder.vertex(matrix, maxX, minY, maxZ);

		bufferBuilder.vertex(matrix, maxX, minY, maxZ);
		bufferBuilder.vertex(matrix, minX, minY, maxZ);

		bufferBuilder.vertex(matrix, minX, minY, maxZ);
		bufferBuilder.vertex(matrix, minX, minY, minZ);

		bufferBuilder.vertex(matrix, minX, minY, minZ);
		bufferBuilder.vertex(matrix, minX, maxY, minZ);

		bufferBuilder.vertex(matrix, maxX, minY, minZ);
		bufferBuilder.vertex(matrix, maxX, maxY, minZ);

		bufferBuilder.vertex(matrix, maxX, minY, maxZ);
		bufferBuilder.vertex(matrix, maxX, maxY, maxZ);

		bufferBuilder.vertex(matrix, minX, minY, maxZ);
		bufferBuilder.vertex(matrix, minX, maxY, maxZ);

		bufferBuilder.vertex(matrix, minX, maxY, minZ);
		bufferBuilder.vertex(matrix, maxX, maxY, minZ);

		bufferBuilder.vertex(matrix, maxX, maxY, minZ);
		bufferBuilder.vertex(matrix, maxX, maxY, maxZ);

		bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
		bufferBuilder.vertex(matrix, minX, maxY, maxZ);

		bufferBuilder.vertex(matrix, minX, maxY, maxZ);
		bufferBuilder.vertex(matrix, minX, maxY, minZ);

		RenderSystem.setShader(ShaderProgramKeys.POSITION);

		RenderSystem.setShaderColor(r,g,b,a);

		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

		RenderSystem.setShaderColor(1,1,1,1);
	}

	public static Color getEspColor(Entity e) {
		Color c;

		if (EntityUtil.isAnimal(e)) {
			c = Colors.PASSIVE;
		} else if (EntityUtil.isMob(e)) {
			c = Colors.HOSTILE;
		} else if (EntityUtil.isPlayer(e)) {
			if (FriendManager.has(e.getName().getString())) {
				c = Colors.PLAYER;
			} else {
				c = Colors.WARN;
			}
		} else {
			return Colors.ELSE;
		}
		return c;
	}

	public static void drawOutline(DrawContext context, int x, int y, int width, int height) {
		context.drawHorizontalLine(x, x + width - 1, y - 1, Colors.BORDER.hashCode()); // top
		context.drawHorizontalLine(x, x + width - 1, y + height, Colors.BORDER.hashCode()); // bottom
		context.drawVerticalLine(x - 1, y - 1, y + height, Colors.BORDER.hashCode()); // left
		context.drawVerticalLine(x + width, y - 1, y + height, Colors.BORDER.hashCode()); // right
	}
}
