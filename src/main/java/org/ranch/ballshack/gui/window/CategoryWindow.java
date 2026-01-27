package org.ranch.ballshack.gui.window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryWindow {

	public int x;
	public int y;

	public static final int width = 85;
	public static final int height = 11;
	private int totalHeight = height;

	public final String title;

	public boolean opened;

	final ModuleCategory category;

	private boolean dragging;
	private int dragX;
	private int dragY;

	public final List<ModuleWidget> moduleWidgets = new ArrayList<>();

	public CategoryWindow(int x, int y, String title, boolean opened, ModuleCategory category) {
		this.x = x;
		this.y = y;
		this.title = title;
		this.opened = opened;
		this.category = category;
		loadModules();
	}

	public void loadModules() {
		List<Module> modules = ModuleManager.getModules();

		for (Module module : modules) {
			if (module.getCategory() == category) {
				moduleWidgets.add(new ModuleWidget(module));
			}
		}
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta, Screen screen) {

		int addedHeight = 0;

		if (dragging) {
			x = mouseX - dragX;
			y = mouseY - dragY;
		}

		x = Math.max(x, 0);
		y = Math.max(y, 0);
		x = Math.min(x, screen.width - width);
		y = Math.min(y, screen.height - height);

		if (opened) {
			for (ModuleWidget moduleWidget : moduleWidgets) {
				addedHeight += moduleWidget.render(context, x, y + height + addedHeight, width, height, mouseX, mouseY);
			}
		}

		DrawUtil.drawHorizontalGradient(context, x, y, width, height, Colors.CLICKGUI_TITLE_START, Colors.CLICKGUI_TITLE_END, width / 8);

		int bottomY = y + height + addedHeight;

		context.drawHorizontalLine(x, x + width - 1, y - 1, Colors.BORDER.hashCode()); // top
		context.drawHorizontalLine(x, x + width - 1, bottomY, Colors.BORDER.hashCode()); // bottom
		context.drawVerticalLine(x - 1, y - 1, bottomY, Colors.BORDER.hashCode()); // left
		context.drawVerticalLine(x + width, y - 1, bottomY, Colors.BORDER.hashCode()); // right

		totalHeight = height + addedHeight;

		/* window title */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		context.drawText(textRend, title, x + 2, y + textInset, Color.WHITE.hashCode(), true);
		// collapsed thinger
		context.drawText(textRend, opened ? "-" : "+", x + width - 8, y + textInset, Color.WHITE.hashCode(), true);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		MinecraftClient mc = MinecraftClient.getInstance();

		if (opened) {
			for (ModuleWidget moduleWidget : moduleWidgets) {
				if (moduleWidget.mouseClicked(mouseX, mouseY, button)) {
					return true;
				}
			}
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				dragging = true;
				dragX = (int) mouseX - x;
				dragY = (int) mouseY - y;
				return true;
			} else if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, totalHeight)) {
				return true;
			}
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				opened = !opened;
				mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				return true;
			} else return GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, totalHeight);
		}
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (opened) {
			for (ModuleWidget moduleWidget : moduleWidgets) {
				moduleWidget.mouseReleased(mouseX, mouseY, button);
			}
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			dragging = false;
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (opened) {
			for (ModuleWidget moduleWidget : moduleWidgets) {
				moduleWidget.keyPressed(keyCode, scanCode, modifiers);
			}
		}
	}

	public boolean charTyped(char chr, int modifiers) {
		if (opened) {
			for (ModuleWidget moduleWidget : moduleWidgets) {
				if (moduleWidget.charTyped(chr, modifiers)) {
					return true;
				}
			}
		}
		return false;
	}

	public int getHeight() {
		return totalHeight;
	}
}
