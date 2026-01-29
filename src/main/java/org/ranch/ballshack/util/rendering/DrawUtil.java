package org.ranch.ballshack.util.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerLikeState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2i;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.util.TextUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

import static org.ranch.ballshack.BallsHack.mc;

public class DrawUtil {
	public static void drawHorizontalGradient(DrawContext context, int x, int y, int width, int height, Color startColor, Color endColor, int resolution) {
		// forgive me
		width += 1;
		height += 1;
		y -= 1;
		int cX = x;
		int i = 0;
		int blend = 0;
		for (; ; ) {
			for (int j = 0; j < resolution; j++) {
				context.drawVerticalLine(cX, y, y + height, blendColor(startColor, endColor, (float) blend / width).hashCode());
				cX++;
				i++;
				if (i >= width) return;
			}
			blend += resolution;
		}
	}

	public static void drawPoint(DrawContext context, int x, int y, Color color) {
		context.fill(x, y, x + 1, y + 1, color.hashCode());
	}

	public static int getScreenWidth() {
		return mc.getWindow().getScaledWidth();
	}

	public static int getScreenHeight() {
		return mc.getWindow().getScaledHeight();
	}

	public static void drawLine(DrawContext context, int x1, int y1, int x2, int y2, Color color) {
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;
		int err = dx - dy;

		while (true) {
			drawPoint(context, x1, y1, color);

			if (x1 == x2 && y1 == y2) break;
			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				x1 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y1 += sy;
			}
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
		for (; ; ) {
			for (int j = 0; j < resolution; j++) {
				context.drawHorizontalLine(x, x + width, cY, blendColor(startColor, endColor, (float) blend / height).hashCode());
				cY++;
				i++;
				if (i >= height) return;
			}
			blend += resolution;
		}
	}

	// pos is offset from cam
	public static void renderText3D(Text text, Vec3d pos, MatrixStack stack, CameraRenderState state, OrderedRenderCommandQueue queue, float dist) {
		queue.submitLabel(
				stack,
				pos,
				0,
				text,
				true,
				Integer.MAX_VALUE,
				dist,
				state
		);
	}

	public static void unBobView(MatrixStack matrices, float tickProgress) {
		if (mc.getCameraEntity() instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
			ClientPlayerLikeState clientPlayerLikeState = abstractClientPlayerEntity.getState();
			float f = clientPlayerLikeState.getReverseLerpedDistanceMoved(tickProgress);
			float g = clientPlayerLikeState.lerpMovement(tickProgress);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-Math.abs(MathHelper.cos(f * (float) Math.PI - 0.2F) * g) * 5.0F));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-MathHelper.sin(f * (float) Math.PI) * g * 3.0F));
			matrices.translate(-MathHelper.sin(f * (float) Math.PI) * g * 0.5F, Math.abs(MathHelper.cos(f * (float) Math.PI) * g), 0.0F);
		}
	}

	public static Color blendColor(Color color1, Color color2, float ratio) {

		int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
		int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
		int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);

		return new Color(Math.min(red, 255), Math.min(green, 255), Math.min(blue, 255));
	}

	public static void drawOutline(DrawContext context, int x, int y, int width, int height, Color color) {
		context.drawHorizontalLine(x, x + width - 1, y - 1, color.hashCode()); // top
		context.drawHorizontalLine(x, x + width - 1, y + height, color.hashCode()); // bottom
		context.drawVerticalLine(x - 1, y - 1, y + height, color.hashCode()); // left
		context.drawVerticalLine(x + width, y - 1, y + height, color.hashCode()); // right
	}

	public static void drawOutline(DrawContext context, int x, int y, int width, int height) {
		drawOutline(context, x, y, width, height, Colors.BORDER);
	}

	private static Vector2i tPos; // IDK what im doing
	private static String tTip;

	public static void queueTooltip(int x, int y, String tooltip) {
		if (tooltip == null) return;
		tPos = new Vector2i(x, y);
		tTip = tooltip;
	}

	public static void clearTooltip() {
		tPos = null;
		tTip = null;
	}

	public static void drawTooltip(DrawContext context) {
		if (tTip == null) return;
		List<String> split = TextUtil.wrapTextKeep(tTip, 20);
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int tWidth = textRend.getWidth(split.stream().sorted(Comparator.comparingInt(textRend::getWidth)).toList().get(split.size() - 1));
		int tHeight = (textRend.fontHeight * split.size()) + 2;
		context.fill(tPos.x, tPos.y, tPos.x + tWidth, tPos.y + tHeight, Color.BLACK.hashCode());
		for (int i = 0; i < split.size(); i++) {
			context.drawText(textRend, split.get(i), tPos.x + 1, tPos.y + 1 + (textRend.fontHeight * i), Color.WHITE.hashCode(), true);
		}
	}
}
