package org.ranch.ballshack.gui.windows.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.windows.WindowScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;
import org.ranch.ballshack.setting.ModuleSettingsGroup;

import java.awt.*;

public class ModuleSettingScreen extends WindowScreen {

	private Module module;

	public ModuleSettingScreen(Module module, Screen parent) {
		super(parent);
		this.module = module;
	}

	@Override
	public void init() {
		super.init();

		int w = 110;
		int s = 10;

		int i = 0;
		for (ModuleSettingsGroup group : module.getSettings()) {
			if (group.getSettings().isEmpty()) continue;
			addChild(new ModuleSettingWindow(
					group,
					i++ * (w + s) + s, textRenderer.fontHeight + s * 2,
					w, 0
			));
		}
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
