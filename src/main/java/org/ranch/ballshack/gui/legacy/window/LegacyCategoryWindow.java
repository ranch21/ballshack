package org.ranch.ballshack.gui.legacy.window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.legacy.LegacyClickGuiScreen;
import org.ranch.ballshack.gui.legacy.LegacyColors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

public class LegacyCategoryWindow {

	public int x;
	public int y;

	public int width = 50;
	public int height = 15;

	public int moduleInset = 4;

	public String title;

	public boolean opened;
	private boolean dragging;
	private int dragX;
	private int dragY;

	private final ModuleCategory category;

	public List<LegacyModuleWidget> moduleWidgets = new ArrayList<LegacyModuleWidget>();

	public LegacyCategoryWindow(int x, int y, String title, boolean opened, ModuleCategory category) {
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
				moduleWidgets.add(new LegacyModuleWidget(module));
			}
		}
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta, LegacyClickGuiScreen screen) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;

		if (dragging) {
			x = mouseX - dragX;
			y = mouseY - dragY;
		}

		x = Math.max(x, 0);
		y = Math.max(y, 0);
		x = Math.min(x, screen.width - width);
		y = Math.min(y, screen.height - height);

		int addedHeight = 0;

		for (LegacyModuleWidget moduleWidget : moduleWidgets) {
			int newY = y + addedHeight + height;
			context.fill(x, newY, x + width, newY + height + moduleInset / 2, LegacyColors.CLICKGUI_1.hashCode());

			addedHeight += moduleWidget.render(context, x + moduleInset / 2, y + height + addedHeight, width - moduleInset, height, mouseX, mouseY);
		}

		context.fill(x, y, x + width, y + height, LegacyColors.CLICKGUI_1.hashCode());

		/* window title */
		context.drawText(textRend, Text.literal(title), x + 2, y + 2, 0xFFFFFFFF, true);
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {

		for (LegacyModuleWidget moduleWidget : moduleWidgets) {
			moduleWidget.mouseClicked(mouseX, mouseY, button);
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
				dragging = true;
				dragX = (int) mouseX - x;
				dragY = (int) mouseY - y;
			}
		}
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {

		for (LegacyModuleWidget moduleWidget : moduleWidgets) {
			moduleWidget.mouseReleased(mouseX, mouseY, button);
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			dragging = false;
		}
	}
}
