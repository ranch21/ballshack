package org.ranch.ballshack.module.modules;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.ClickGuiScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class ClickGui extends Module {
	public ClickGui() {
		super("ClickGui", ModuleCategory.RENDER, GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		MinecraftClient.getInstance().setScreen(new ClickGuiScreen());
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
