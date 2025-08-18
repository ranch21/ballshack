package org.ranch.ballshack.gui;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector2d;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.balls.Ball;
import org.ranch.ballshack.gui.balls.BallHandler;
import org.ranch.ballshack.gui.balls.Rect;
import org.ranch.ballshack.gui.window.CategoryWindow;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {

	ButtonWidget button;
	private static final List<CategoryWindow> windows = new ArrayList<>();
	public static Setting<List<WindowData>> windowData = new Setting<>(new ArrayList<>(), "windowData", new TypeToken<List<WindowData>>() {
	}.getType());

	BallHandler ballHandler;
	boolean ballsEnabled = false;
	boolean darken = true;

	public ClickGuiScreen() {
		super(NarratorManager.EMPTY);
	}


	private void loadCategories() {
		boolean saveData = !windowData.getValue().isEmpty();
		int i = 0;
		for (ModuleCategory category : ModuleCategory.values()) {
			if (ModuleManager.getModulesByCategory(category).isEmpty()) continue;

			int x = saveData ? windowData.getValue().get(i).x : 3;
			int y = saveData ? windowData.getValue().get(i).y : (14 * i) + 3;

			CategoryWindow win = new CategoryWindow(x, y, StringUtils.capitalize(category.name().toLowerCase()), saveData && windowData.getValue().get(i).opened, category);

			windows.add(win);
			if (!saveData) {
				List<WindowData> data = windowData.getValue();
				data.add(new WindowData(x, y, false));
				windowData.setValue(data);
			}

			i++;
		}
	}

	protected void init() {
		ballHandler = new BallHandler();
		ballHandler.spawnBalls(50, this.width, this.height);
		if (windows.isEmpty()) {
			loadCategories();
		}
	}

	public void setSettings(ModuleSettings settings) {
		DropDown dropDown = (DropDown) settings.getSetting(0);

		boolean balls = (boolean) dropDown.getSetting(0).getValue();
		int amount = (int) (double) dropDown.getSetting(1).getValue();
		double gravity = (double) dropDown.getSetting(2).getValue();
		//double bounce = (double) dropDown.getSetting(3).getValue();
		double size = (double) dropDown.getSetting(3).getValue();
		boolean windowCollide = (boolean) dropDown.getSetting(4).getValue();
		boolean darken = (boolean) settings.getSetting(1).getValue();

		ballHandler.gravity = gravity * 2;
		ballsEnabled = balls;
		ballHandler.winCollide = windowCollide;
		Ball.size = (int) size;
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
			ArrayList<Rect> rects = new ArrayList<>();
			for (CategoryWindow win : windows) {
				rects.add(new Rect(new Vector2d(win.x, win.y), new Vector2d(CategoryWindow.width, win.getHeight())));
			}
			ballHandler.update(this.width, this.height, delta, rects);
			ballHandler.render(context);
		}

		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		DrawUtil.drawText(context, textRend, BallsHack.title.getValue(), 5, 5, Colors.PALLETE_1, true);
		DrawUtil.drawText(context, textRend, BallsHack.version, 5 + textRend.getWidth(BallsHack.title.getValue() + " "), 5, Color.WHITE, true);

		int i = 0;
		for (CategoryWindow window : windows) {
			window.render(context, mouseX, mouseY, delta, this);
			List<WindowData> data = windowData.getValue();
			data.set(i, new WindowData(window.x, window.y, window.opened));
			windowData.setValue(data);
			i++;
		}

		DrawUtil.drawTooltip(context);
		DrawUtil.clearTooltip();
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
			window.keyPressed(keyCode, scanCode, modifiers);
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		for (CategoryWindow window : windows) {
			if (window.charTyped(chr, modifiers)) {
				break;
			}
		}

		return super.charTyped(chr, modifiers);
	}

	public boolean shouldPause() {
		return false;
	}
}
