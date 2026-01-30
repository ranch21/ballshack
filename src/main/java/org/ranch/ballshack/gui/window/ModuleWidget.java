package org.ranch.ballshack.gui.window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;

public class ModuleWidget {

	private final Module module;

	private int x;
	private int y;

	private int width;
	private int height;

	private boolean settingsOpen;

	private final ModuleSettings settings;

	public ModuleWidget(Module module) {
		this.module = module;
		this.settings = module.getSettings();
	}

	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		int addedHeight = 0;

		if (settingsOpen) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				addedHeight += setting.renderBase(context, x, y + height + addedHeight, width, height, mouseX, mouseY);
			}
		}

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_1.getColor().brighter().hashCode());
		} else {
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_1.getColor().hashCode());
		}

		/* module name */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;

		int textInset = (height - textRend.fontHeight) / 2;

		context.drawText(textRend, module.getName(), x + 2, y + textInset, Color.WHITE.hashCode(), true);
		if (settingsOpen && !module.isEnabled()) {
			context.drawText(textRend, "*", x + width - 8, y + textInset, Color.WHITE.hashCode(), true);
		} else if (settingsOpen && module.isEnabled() && !module.isMeta()) {
			context.drawText(textRend, "*", x + width - 16, y + textInset, Color.WHITE.hashCode(), true);
			context.drawText(textRend, "#", x + width - 8, y + textInset, Color.WHITE.hashCode(), true);
		} else if (!settingsOpen && module.isEnabled() && !module.isMeta()) {
			context.drawText(textRend, "#", x + width - 8, y + textInset, Color.WHITE.hashCode(), true);
		}

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			DrawUtil.queueTooltip(x + width + 1, y, module.getTooltip());
		}

		return height + addedHeight;
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (settingsOpen) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				setting.mouseClickedBase(mouseX, mouseY, button);
			}
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				module.toggle();
				return true;
			}
		}
		if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				settingsOpen = !settingsOpen;
				return true;
			}
		}
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (settingsOpen) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				setting.mouseReleasedBase(mouseX, mouseY, button);
			}
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (settingsOpen) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				setting.keyPressedBase(keyCode, scanCode, modifiers);
			}
		}
	}

	public boolean charTyped(char chr, int modifiers) {
		if (settingsOpen) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				if (setting.charTypedBase(chr, modifiers)) {
					return true;
				}
			}
		}
		return false;
	}

}
