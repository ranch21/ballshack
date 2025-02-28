package org.ranch.ballshack.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import org.apache.commons.lang3.StringUtils;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.window.CategoryWindow;
import org.ranch.ballshack.module.ModuleCategory;

import java.awt.*;
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
			windows.add(new CategoryWindow((110 * i) + 10, 30, StringUtils.capitalize(category.name().toLowerCase()), true, category));
			i++;
		}
	}

	protected void init() {
		windows.clear();
		loadCategories();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		for (CategoryWindow window : windows) {
			window.render(context, mouseX, mouseY, delta, this);
		}

		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRend, BallsHack.title, 5, 5,Colors.PALLETE_1.hashCode(),true);
		context.drawText(textRend, BallsHack.version, 5 + textRend.getWidth(BallsHack.title + " "), 5, Color.WHITE.hashCode(),true);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		for (CategoryWindow window : windows) {
			if (window.mouseClicked(mouseX, mouseY, button)) {
				break; // if one window uses click, stop sending click
			}
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

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

		for (CategoryWindow window : windows) {
			window.keyPressed(keyCode,scanCode,modifiers);
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	public boolean shouldPause() {
		return false;
	}
}
