package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.clickgui.ClickGuiScreen;
import org.ranch.ballshack.gui.legacy.LegacyClickGuiScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class ClickGui extends Module {

	private ClickGuiScreen screen;

	public ClickGui() {
		super("ClickGui", ModuleCategory.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT, "The module screeent thing", true);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		//screen.setSettings(this);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (false) {
			LegacyClickGuiScreen screen = new LegacyClickGuiScreen();
			MinecraftClient.getInstance().setScreen(screen);
		} else {
			ClickGuiScreen screen = new ClickGuiScreen();
			this.screen = screen;
			MinecraftClient.getInstance().setScreen(screen);
		}
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
