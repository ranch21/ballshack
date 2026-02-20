package org.ranch.ballshack.gui.clickgui;

import net.minecraft.client.gui.Click;
import net.minecraft.client.util.GlfwUtil;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.ButtonWidget;
import org.ranch.ballshack.module.Module;

public class ModuleWindow extends ButtonWidget {

	public ModuleWindow(Module module, int x, int y, int width, int height) {
		super(module.getName(), x, y, width, height, (button, click) -> {
			if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				module.toggle();
			} else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				BallsHack.mc.setScreen(new ModuleSettingScreen(module));
			}
		});
	}
}
