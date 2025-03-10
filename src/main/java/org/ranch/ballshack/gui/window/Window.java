package org.ranch.ballshack.gui.window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

public abstract class Window {
	public int x;
	public int y;

	public int width;
	public int height;

	public int barHeight = 11;

	public String title;

	protected boolean dragging;
	protected int dragX;
	protected int dragY;

	public Window(int x, int y, int width, int height, String title) {
		this.x = x;
		this.y = y;
		this.title = title;
		this.width = width;
		this.height = height;
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta, Screen screen) {

		handleDrag(mouseX, mouseY, screen);

		//context.fill(x, y, x + width, y + height, Colors.CLICKGUI_1.hashCode());
		DrawUtil.drawHorizontalGradient(context, x, y, width, barHeight, Colors.CLICKGUI_TITLE_START, Colors.CLICKGUI_TITLE_END, width/10);

		int bottomY = y + height;

		context.drawHorizontalLine(x, x + width - 1, y - 1, Colors.BORDER.hashCode()); // top
		context.drawHorizontalLine(x, x + width - 1, bottomY, Colors.BORDER.hashCode()); // bottom
		context.drawVerticalLine(x - 1, y - 1, bottomY, Colors.BORDER.hashCode()); // left
		context.drawVerticalLine(x + width, y - 1, bottomY, Colors.BORDER.hashCode()); // right

		/* window title */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (barHeight - textRend.fontHeight) / 2;
		DrawUtil.drawText(context, textRend, title,x + 2,y + textInset, Color.WHITE,true);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, barHeight)) {
				dragging = true;
				dragX = (int) mouseX - x;
				dragY = (int) mouseY - y;
				return true;
			} else if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				return true;
			}
		}

		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			dragging = false;
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {}

	protected void handleDrag(int mouseX, int mouseY, Screen screen) {
		if (dragging) {
			x = mouseX - dragX;
			y = mouseY - dragY;
		}

		x = Math.max(x, 0);
		y = Math.max(y, 0);
		x = Math.min(x, screen.width - width);
		y = Math.min(y, screen.height - height);
	}
}
