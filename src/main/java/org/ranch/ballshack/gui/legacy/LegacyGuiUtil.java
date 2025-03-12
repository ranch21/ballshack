package org.ranch.ballshack.gui.legacy;

public class LegacyGuiUtil {
	public static boolean mouseOverlap(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseOverlap( (double) mouseX, (double) mouseY, x, y, width, height);
	}

	public static boolean mouseOverlap(double mouseX, double mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
}
