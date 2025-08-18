package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.WindowScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class WindowGui extends Module {

	public WindowGui() {
		super("WinGui", ModuleCategory.CLIENT, GLFW.GLFW_KEY_RIGHT_CONTROL, "Window 11 confirmed", true);
	}

	@Override
	public void onEnable() {
		super.onEnable();

		WindowScreen screen = new WindowScreen();
		MinecraftClient.getInstance().setScreen(screen);
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
