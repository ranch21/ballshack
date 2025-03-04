package org.ranch.ballshack.gui.window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.util.DrawUtil;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettings;

import java.awt.*;

public class ModuleWidget {
	
	private Module module;

	private int x;
	private int y;

	private int width;
	private int height;

	private boolean settingsOpen;

	private ModuleSettings settings;
	
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
			for (ModuleSetting<?> setting : settings.getSettings()) {
				addedHeight += setting.render(context, x, y + height + addedHeight, width, height, mouseX, mouseY);
			}
		}

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_2.brighter().hashCode());
		} else {
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_2.hashCode());
		}

		/* module name */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;

		int textInset = (height - textRend.fontHeight) / 2;

		DrawUtil.drawText(context, textRend, module.getName(),x + 2,y + textInset, Color.WHITE,true);
		if (settingsOpen && !module.isEnabled()) {
			DrawUtil.drawText(context, textRend, "*",x + width - 8,y + textInset, Color.WHITE,true);
		} else if (settingsOpen && module.isEnabled()) {
			DrawUtil.drawText(context, textRend, "*",x + width - 16,y + textInset, Color.WHITE,true);
			DrawUtil.drawText(context, textRend, "#",x + width - 8,y + textInset, Color.WHITE,true);
		} else if (!settingsOpen && module.isEnabled()) {
			DrawUtil.drawText(context, textRend, "#",x + width - 8,y + textInset, Color.WHITE,true);
		}

		return height + addedHeight;
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (settingsOpen) {
			for (ModuleSetting<?> setting : settings.getSettings()) {
				if (setting.mouseClicked(mouseX, mouseY, button)) {
					return true;
				}
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
				if (settingsOpen) {
					settingsOpen = false;
				} else {
					settingsOpen = true;
				}
				return true;
			}
		}
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (settingsOpen) {
			for (ModuleSetting<?> setting: settings.getSettings()) {
				setting.mouseReleased(mouseX, mouseY, button);
			}
		}
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (settingsOpen) {
			for (ModuleSetting<?> setting : settings.getSettings()) {
				setting.keyPressed(keyCode, scanCode, modifiers);
			}
		}
	}
	
}
