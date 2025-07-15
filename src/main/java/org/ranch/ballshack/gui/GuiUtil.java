package org.ranch.ballshack.gui;

public class GuiUtil {
	public static boolean mouseOverlap(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseOverlap( (double) mouseX, (double) mouseY, x, y, width, height);
	}

	public static boolean mouseOverlap(double mouseX, double mouseY, int x, int y, int width, int height) {
		if (width < 0) {
			width = -width;
			x -= width;
		}
		if (height < 0) {
			height = -height;
			y -= height;
		}
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
}
