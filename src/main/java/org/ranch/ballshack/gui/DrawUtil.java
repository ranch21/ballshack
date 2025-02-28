package org.ranch.ballshack.gui;

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
				blend++;
				if (i >= width) return;
			}
		}
	}

	public static Color blendColor(Color color1, Color color2, float ratio) {
		float inverse_blending = 1 - ratio;

		float red =   color1.getRed()   * ratio   +   color2.getRed()   * inverse_blending;
		float green = color1.getGreen() * ratio   +   color2.getGreen() * inverse_blending;
		float blue =  color1.getBlue()  * ratio   +   color2.getBlue()  * inverse_blending;

		return new Color (red / 255, green / 255, blue / 255);
	}
}
