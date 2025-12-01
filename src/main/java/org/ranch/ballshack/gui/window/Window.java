package org.ranch.ballshack.gui.window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Window {

	public List<Widget> widgets = new ArrayList<>();

	public int x;
	public int y;

	public int width;
	public int height;

	public int barHeight = 11;

	public String title;
	public boolean opened;

	protected boolean dragging;
	protected int dragX;
	protected int dragY;

	public Window(int x, int y, int width, int height, String title) {
		this.x = x;
		this.y = y;
		this.title = title;
		this.width = width;
		this.height = height;
		this.opened = true;
		setup();
	}

	public abstract void setup();

	public void render(DrawContext context, int mouseX, int mouseY, Screen screen) {

		if (!opened) return;

		handleDrag(mouseX, mouseY, screen);

		//context.fill(x, y, x + width, y + height, Colors.CLICKGUI_1.hashCode());
		DrawUtil.drawHorizontalGradient(context, x, y - barHeight, width, barHeight, Colors.CLICKGUI_TITLE_START, Colors.CLICKGUI_TITLE_END, width / 10);


		DrawUtil.drawOutline(context, x, y - barHeight, width, height + barHeight);

		/* window title */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (barHeight - textRend.fontHeight) / 2;
		DrawUtil.drawText(context, textRend, title, x + 2, y + textInset - barHeight, Color.WHITE, true);

		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

		context.enableScissor(x, y, x + width, y + height);

		for (Widget widget : widgets) {
			widget.render(context, mouseX, mouseY, x, y);
		}

		context.disableScissor();
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (!opened) return false;

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y - barHeight, width, barHeight)) {
				dragging = true;
				dragX = (int) mouseX - x;
				dragY = (int) mouseY - y;
				return true;
			} else if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				for (Widget widget : widgets) {
					widget.mouseClicked(mouseX, mouseY, button);
				}
			}
		}

		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			dragging = false;
		}
		for (Widget widget : widgets) {
			widget.mouseReleased(mouseX, mouseY, button);
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		for (Widget widget : widgets) {
			widget.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	public void charTyped(char chr, int modifiers) {
		for (Widget widget : widgets) {
			widget.charTyped(chr, modifiers);
		}
	}

	protected void handleDrag(int mouseX, int mouseY, Screen screen) {
		if (dragging) {
			x = mouseX - dragX;
			y = mouseY - dragY;
		}

		x = Math.max(x, 0);
		y = Math.max(y, barHeight);
		x = Math.min(x, screen.width - width);
		y = Math.min(y, screen.height - height);
	}
}
