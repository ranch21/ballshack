package org.ranch.ballshack.module.modules;

import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class Flight extends Module {
	public Flight() {
		super("Flight", ModuleCategory.MOVEMENT, GLFW.GLFW_KEY_F);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		mc.player.getAbilities().flying = true;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.player.getAbilities().flying = false;
	}
}
