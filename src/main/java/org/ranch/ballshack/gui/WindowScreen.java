package org.ranch.ballshack.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.window.Window;
import org.ranch.ballshack.gui.window.windows.SettingsWindow;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class WindowScreen extends Screen {

	private static List<Window> windows = Arrays.asList(
			new SettingsWindow(10,10)
	);

	public WindowScreen() {
		super(NarratorManager.EMPTY);
	}

	public static Window getWindow(Class<? extends Window> winClass) {
		return windows.stream().filter(w -> w.getClass().equals(winClass)).findFirst().orElse(null);
	}

	@Override
	public void tick() {
		super.tick();
		MinecraftClient mc = MinecraftClient.getInstance();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {

		super.render(context, mouseX, mouseY, delta);

		context.fill(0, 0, width, height, Colors.CLICKGUI_BACKGROUND.hashCode());

		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRend, BallsHack.title, 5, 5,Colors.PALLETE_1.hashCode(),true);
		context.drawText(textRend, BallsHack.version, 5 + textRend.getWidth(BallsHack.title + " "), 5, Color.WHITE.hashCode(),true);

		for (Window window : windows) {
			window.render(context, mouseX, mouseY, this);
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

		for (Window window : windows) {
			window.mouseReleased(mouseX, mouseY, button);
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

		for (Window window : windows) {
			window.keyPressed(keyCode,scanCode,modifiers);
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	public boolean shouldPause() {
		return false;
	}
}

