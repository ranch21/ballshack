package org.ranch.ballshack.module.modules;

import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class TestModule extends Module {
	public TestModule() {
		super("test", ModuleCategory.MISC, GLFW.GLFW_KEY_U);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		mc.player.setYaw(mc.player.getYaw() + 1.0f);
		BallsLogger.info("i love ballsacks");
	}
}
