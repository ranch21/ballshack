package org.ranch.ballshack.module.modules;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;

import java.util.List;

public class Boatfly extends Module {

	public Boatfly() {
		super(
			"BoatFly",
			ModuleCategory.MOVEMENT,
			GLFW.GLFW_KEY_G,
			new ModuleSettings(List.of(new SettingSlider(2, "Vspeed", 1, 4, 0.5)))
		);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (!mc.player.hasVehicle()) return;

		Entity boat = mc.player.getVehicle();

		Vec3d velocity = boat.getVelocity();

		double xVel = velocity.x;
		double yVel = 0;
		double zVel = velocity.z;

		double verticalSpeed = (double) this.getSettings().getSetting(0).getValue() / 3;

		if (mc.options.jumpKey.isPressed()) {
			yVel = verticalSpeed;
		} else if (mc.options.sprintKey.isPressed()) {
			yVel = -verticalSpeed;
		}

		boat.setVelocity(xVel, yVel, zVel);
	}
}
