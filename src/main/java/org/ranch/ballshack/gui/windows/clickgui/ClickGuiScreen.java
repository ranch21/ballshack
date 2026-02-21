package org.ranch.ballshack.gui.windows.clickgui;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.WindowData;
import org.ranch.ballshack.gui.windows.ConsoleWindow;
import org.ranch.ballshack.gui.windows.WindowScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;
import org.ranch.ballshack.setting.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends WindowScreen {

	// how the fuck did that buttonwidget survive so much :sob: :sob: :sob: :sob:
	// update: its back
	ButtonWidget button;
	public static final Setting<List<WindowData>> windowData = new Setting<>(new ArrayList<>(), "windowData", new TypeToken<List<WindowData>>() {
	}.getType());


	public ClickGuiScreen(Screen parent) {
		super(parent);
	}

	@Override
	public void init() {
		super.init();

		int s = 10;
		int w = 70;
		int i = 0;
		for (ModuleCategory category : ModuleCategory.values()) {
			List<Module> modules = ModuleManager.getModulesByCategory(category);

			if (modules.isEmpty()) continue;

			addChild(new CategoryWindow(
					category,
					i++ * (s + w) + s, textRenderer.fontHeight + s * 2, w, 100
			));
		}

		addChild(new ConsoleWindow("Console", 50,5, 4 * 40, 3 * 40));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRend, BallsHack.title.getValue(), 5, 5, Colors.PALETTE_1.getColor().hashCode(), true);
		context.drawText(textRend, BallsHack.version, 5 + textRend.getWidth(BallsHack.title.getValue() + " "), 5, Color.WHITE.hashCode(), true);
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (input.getKeycode() == GLFW.GLFW_KEY_ESCAPE)
			client.setScreen(parent);
		return super.keyPressed(input);
	}
}
