package org.ranch.ballshack.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import org.apache.commons.lang3.StringUtils;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.balls.BallHandler;
import org.ranch.ballshack.gui.window.CategoryWindow;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.DropDown;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {

	ButtonWidget button;
	List<CategoryWindow> windows = new ArrayList<>();

	BallHandler ballHandler;
	boolean ballsEnabled = false;
	boolean darken = true;

	public ClickGuiScreen() {
		super(NarratorManager.EMPTY);
	}

	private void loadCategories() {
		int i = 0;
		for (ModuleCategory category : ModuleCategory.values()) {
			if (ModuleManager.getModulesByCategory(category).isEmpty()) continue;

			windows.add(new CategoryWindow(((CategoryWindow.width + 3) * i) + 2, 30, StringUtils.capitalize(category.name().toLowerCase()), true, category));
			i++;
		}
	}

	protected void init() {
		ballHandler = new BallHandler();
		ballHandler.spawnBalls(50, this.width, this.height);
		windows.clear();
		loadCategories();
	}

	public void setSettings(ModuleSettings settings) {
		DropDown dropDown = (DropDown) settings.getSetting(0);

		boolean balls = (boolean) dropDown.getSetting(0).getValue();
		int amount = (int) (double) dropDown.getSetting(1).getValue();
		double gravity = (double) dropDown.getSetting(2).getValue();
		double bounce = (double) dropDown.getSetting(3).getValue();
		boolean darken = (boolean) settings.getSetting(1).getValue();

		ballHandler.gravity = gravity * 2;
		ballHandler.bounce = bounce;
		ballsEnabled = balls;
		this.darken = darken;

		if (ballHandler.getBallCount() != amount) {
			ballHandler.clearBalls();
			ballHandler.spawnBalls(amount, this.width, this.height);
		}
	}

	@Override
	public void tick() {
		super.tick();
		MinecraftClient mc = MinecraftClient.getInstance();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {

		super.render(context, mouseX, mouseY, delta);

		if (darken) {
			context.fill(0, 0, width, height, Colors.CLICKGUI_BACKGROUND.hashCode());
		}

		if (ballsEnabled) {
			ballHandler.render(context);
			ballHandler.update(this.width, this.height, delta);
		}

		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRend, BallsHack.title, 5, 5,Colors.PALLETE_1.hashCode(),true);
		context.drawText(textRend, BallsHack.version, 5 + textRend.getWidth(BallsHack.title + " "), 5, Color.WHITE.hashCode(),true);

		for (CategoryWindow window : windows) {
			window.render(context, mouseX, mouseY, delta, this);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		for (int i = windows.size() - 1; i >= 0; i--) {
			if (windows.get(i).mouseClicked(mouseX, mouseY, button)) {
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
