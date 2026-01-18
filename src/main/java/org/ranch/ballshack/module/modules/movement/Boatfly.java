package org.ranch.ballshack.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

import java.util.List;

public class Boatfly extends Module {

	public Boatfly() {
		super(
				"BoatFly",
				ModuleCategory.MOVEMENT,
				0,
				new ModuleSettings(List.of(
						new SettingSlider(2, "Vspeed", 1, 4, 0.5).featured(),
						new SettingSlider(2, "Hspeed", 1, 10, 0.5)
				)), "popboat"
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
		double horizontalSpeed = (double) this.getSettings().getSetting(1).getValue() / 3;

		if (mc.options.jumpKey.isPressed()) {
			yVel = verticalSpeed;
		} else if (mc.options.sprintKey.isPressed()) {
			yVel = -verticalSpeed;
		}

		if (mc.options.forwardKey.isPressed()) {
			double speed = horizontalSpeed;
			float yawRad = boat.getYaw() * MathHelper.RADIANS_PER_DEGREE;

			xVel = MathHelper.sin(-yawRad) * speed;
			zVel = MathHelper.cos(yawRad) * speed;
		}

		boat.setVelocity(xVel, yVel, zVel);
	}
}
