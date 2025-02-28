package org.ranch.ballshack.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import org.ranch.ballshack.gui.window.CategoryWindow;
import org.ranch.ballshack.module.ModuleCategory;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {

	ButtonWidget button;
	List<CategoryWindow> windows = new ArrayList<>();

	public ClickGuiScreen() {
		super(NarratorManager.EMPTY);
	}

	private void loadCategories() {
		int i = 0;
		for (ModuleCategory category : ModuleCategory.values()) {
			windows.add(new CategoryWindow((60 * i) + 10, 10, category.name(), true, category));
			i++;
		}
	}

	protected void init() {
		loadCategories();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		for (CategoryWindow window : windows) {
			window.render(context, mouseX, mouseY, delta, this);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		for (CategoryWindow window : windows) {
			window.mouseClicked(mouseX, mouseY, button);
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {

		for (CategoryWindow window : windows) {
			window.mouseReleased(mouseX, mouseY, button);
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	public boolean shouldPause() {
		return false;
	}
}
