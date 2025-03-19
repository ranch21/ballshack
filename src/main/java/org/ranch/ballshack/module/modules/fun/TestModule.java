package org.ranch.ballshack.module.modules.fun;

import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.PlayerUtil;

import java.awt.*;
import java.util.Arrays;

public class TestModule extends Module {
	public TestModule() {
		super("Test", ModuleCategory.PLAYER, GLFW.GLFW_KEY_R, new ModuleSettings(Arrays.asList(
				new SettingSlider(5, "slider", 1, 10, 0.25),
				new SettingMode( 0, "mode", Arrays.asList("one", "two", "three")),
				new DropDown("d1", Arrays.asList(
						new SettingToggle(true, "depth1"),
						new DropDown("d2", Arrays.asList(
								new SettingToggle(true, "depth2"),
								new DropDown("d3", Arrays.asList(
										new SettingToggle(true, "depth3")
								))
						))
				)),
				new SettingColor("t", Color.BLUE)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		mc.player.setYaw(mc.player.getYaw() + 1.0f); //the first thing a module did

		PlayerUtil.setMovement(new Vec3d(0, 0, 1));
	}
}
