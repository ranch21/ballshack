package org.ranch.ballshack.setting.settings;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

// BIGGEST SPAGHETTI EVER MADE

public class SettingColor extends ModuleSetting<Color> {

	boolean holdingSquare;
	boolean holdingBar;
	boolean opened;

	int cubeX;
	int cubeY;
	int cubeSize;
	int barX;
	int barY;
	int barWidth = 4;

	int[] cursorPos;
	int cursorY;

	public SettingColor(String name, Color value) {
		super(name, value);

		float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);

		int cursorX = (int) (hsb[0] * cubeSize);
		int cursorY = (int) (-(hsb[1] - 1) * cubeSize);

		cursorPos = new int[] {cursorX, cursorY};
		cursorY = (int) (-(hsb[2] - 1) * cubeSize);
	}

	@Override
	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		int addedHeight = 0;

		context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.hashCode());

		if (opened) {

			cubeX = x + 1;
			cubeY = y + height + 1;
			cubeSize = width - barWidth - 4 - 4;
			barX = cubeX + cubeSize + 4;
			barY = cubeY;

			if (holdingSquare) {
				getCursorPos(mouseX, mouseY);
			}
			if (holdingBar) {
				this.cursorY = Math.max(Math.min(mouseY - cubeY, cubeSize), 0);
			}

			context.fill(x, y+height, x+width, y+cubeSize+height + 2, Colors.CLICKGUI_3.hashCode());

			addedHeight += cubeSize + 2;

			for (int i = 0; i < cubeSize; i++) {
				float hue = (float) i / cubeSize;

				Color start = Color.getHSBColor(hue, 1, 1);
				Color end = Color.getHSBColor(hue, 0.01f, 1);

				DrawUtil.drawVerticalGradient(context, cubeX + i, cubeY, 1, cubeSize, start, end, 1);
			}

			float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);

			Color valueBright = Color.getHSBColor(hsb[0], hsb[1], 1);

			DrawUtil.drawVerticalGradient(context, barX, barY, barWidth, cubeSize, valueBright, Color.BLACK, 1);
			context.drawHorizontalLine(barX, barX + barWidth + 1, cursorY + cubeY - 1, Color.WHITE.hashCode());

			drawCursor(context, cursorPos[0], cursorPos[1]);


			value = Color.getHSBColor((float) cursorPos[0] / cubeSize, -(((float) cursorPos[1] / cubeSize) - 1), hsb[2]);

			hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);

			value = Color.getHSBColor(hsb[0], hsb[1], -(((float) cursorY / cubeSize) - 1));
		}

		/* setting name */
		drawText(context, this.getName());
		drawTextRightAligned(context, opened ? "-" : "+");

		return height + addedHeight;
	}

	private void getCursorPos(int mouseX, int mouseY) {
		int cursorX = Math.max(Math.min(mouseX - cubeX, cubeSize), 0);
		int cursorY = Math.max(Math.min(mouseY - cubeY, cubeSize), 0);

		cursorPos = new int[] {cursorX, cursorY};
	}

	private void drawCursor(DrawContext context, int rx, int  ry) {

		int x = cubeX + rx;
		int y = cubeY + ry;

		int lineSize = 2;

		context.drawHorizontalLine(x-lineSize, x-1, y-1, Color.WHITE.hashCode());
		context.drawHorizontalLine(x+lineSize, x+1, y-1, Color.WHITE.hashCode());

		context.drawVerticalLine(x, y-1, y+lineSize, Color.WHITE.hashCode());
		context.drawVerticalLine(x, y-1, y-lineSize-2, Color.WHITE.hashCode());
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			opened = !opened;
		} if (opened && GuiUtil.mouseOverlap(mouseX, mouseY, cubeX, cubeY, cubeSize, cubeSize) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			holdingSquare = true;
		} if (opened && GuiUtil.mouseOverlap(mouseX, mouseY, barX, barY, barWidth + 1, cubeSize) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			holdingBar = true;
		}
		return false;
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		holdingSquare = false;
		holdingBar = false;
	}

	@Override
	public String getFormattedValue() {
		return String.valueOf(value.hashCode());
	}
}
