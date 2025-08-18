package org.ranch.ballshack.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventSetSneaking;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.List;

public class Flight extends Module {

	private int antiKick = 0;

	public Flight() {
		super("Flight", ModuleCategory.MOVEMENT, GLFW.GLFW_KEY_F, new ModuleSettings(List.of(
				new SettingSlider(2, "Hspeed", 0.5, 10, 0.5).featured(),
				new SettingSlider(2, "Vspeed", 0.5, 5, 0.5)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.hasVehicle()) return;

		double horizontalSpeed = (double) this.getSettings().getSetting(0).getValue() / 3;
		double verticalSpeed = (double) this.getSettings().getSetting(1).getValue() / 3;

		mc.player.setVelocity(PlayerUtil.getMovementVector(horizontalSpeed, verticalSpeed));

		if (antiKick % 2 == 0) {
			mc.player.setVelocity(mc.player.getVelocity().add(0, -0.08, 0));
		} else {
			mc.player.setVelocity(mc.player.getVelocity().add(0, 0.08, 0));
		}

		antiKick++;
	}

	@EventSubscribe
	public void onSneak(EventSetSneaking event) {
		event.cancel();
	}
}
