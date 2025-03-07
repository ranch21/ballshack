package org.ranch.ballshack.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

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
}
