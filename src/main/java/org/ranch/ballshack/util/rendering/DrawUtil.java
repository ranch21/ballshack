package org.ranch.ballshack.util.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.joml.Vector2i;
import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.util.EntityUtil;

import java.awt.*;

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

	public static Color blendColor(Color color1, Color color2, float ratio) {

		int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
		int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
		int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);

		return new Color(Math.min(red, 255), Math.min(green, 255), Math.min(blue, 255));
	}

	public static void drawTextRight(DrawContext context, TextRenderer textRend, String text, int x, int y, Color color, boolean shadow) {
		drawText(context, textRend, text, x - textRend.getWidth(text), y, color, shadow);
	}

	public static void drawTextRight(DrawContext context, TextRenderer textRend, Text text, int x, int y, Color color, boolean shadow) {
		drawText(context, textRend, text, x - textRend.getWidth(text), y, color, shadow);
	}

	@Deprecated
	public static void drawText(DrawContext context, TextRenderer textRend, String text, int x, int y, Color color, boolean shadow) {

		context.drawText(textRend, text, x, y, color.hashCode(), shadow);

		/*textRend.draw(
				text,
				x,
				y,
				color.hashCode(),
				shadow,
				context.getMatrices().peek().getPositionMatrix(),
				((DrawContextAccessor) context).getVertexConsumers(),
				TextRenderer.TextLayerType.SEE_THROUGH,
				0,
				15728880
		);*/
	}

	@Deprecated
	public static void drawText(DrawContext context, TextRenderer textRend, Text text, int x, int y, Color color, boolean shadow) {

		context.drawText(textRend, text, x, y, color.hashCode(), shadow);

		/*textRend.draw(
				text,
				x,
				y,
				color.hashCode(),
				shadow,
				context.getMatrices().peek().getPositionMatrix(),
				((DrawContextAccessor) context).getVertexConsumers(),
				TextRenderer.TextLayerType.SEE_THROUGH,
				0,
				15728880
		);*/
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

	public static void drawOutline(DrawContext context, int x, int y, int width, int height, Color color) {
		context.drawHorizontalLine(x, x + width - 1, y - 1, color.hashCode()); // top
		context.drawHorizontalLine(x, x + width - 1, y + height, color.hashCode()); // bottom
		context.drawVerticalLine(x - 1, y - 1, y + height, color.hashCode()); // left
		context.drawVerticalLine(x + width, y - 1, y + height, color.hashCode()); // right
	}

	public static void drawOutline(DrawContext context, int x, int y, int width, int height) {
		drawOutline(context, x, y, width, height, Colors.BORDER);
	}

	public static Vector2i tPos; // IDK what im doing
	public static String tTip;

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
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int tWidth = textRend.getWidth(tTip) + 2;
		int tHeight = textRend.fontHeight + 2;
		context.fill(tPos.x, tPos.y, tPos.x + tWidth, tPos.y + tHeight, Color.BLACK.hashCode());
		context.drawText(textRend, tTip, tPos.x + 1, tPos.y + 1, Color.WHITE.hashCode(), true);
	}
}
