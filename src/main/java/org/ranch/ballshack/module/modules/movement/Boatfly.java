package org.ranch.ballshack.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

public class Boatfly extends Module {

	public final SettingSlider hSpeed = dGroup.add(new SettingSlider("Hspeed", 2, 1, 10, 0.5).featured());
	public final SettingSlider vSpeed = dGroup.add(new SettingSlider("Vspeed", 2, 1, 4, 0.5));

	public Boatfly() {
		super(
				"BoatFly",
				ModuleCategory.MOVEMENT,
				0, "popboat <- best tooltip oat"
		);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player == null || !mc.player.hasVehicle())
			return;

		Entity boat = mc.player.getVehicle();

		if (boat == null)
			return;

		Vec3d velocity = boat.getVelocity();

		double xVel = velocity.x;
		double yVel = 0;
		double zVel = velocity.z;

		double verticalSpeed = vSpeed.getValue() / 3;
		double horizontalSpeed = hSpeed.getValue() / 3;

		if (mc.options.jumpKey.isPressed()) {
			yVel = verticalSpeed;
		} else if (mc.options.sprintKey.isPressed()) {
			yVel = -verticalSpeed;
		}

		if (mc.options.forwardKey.isPressed()) {
			float yawRad = boat.getYaw() * MathHelper.RADIANS_PER_DEGREE;

			xVel = MathHelper.sin(-yawRad) * horizontalSpeed;
			zVel = MathHelper.cos(yawRad) * horizontalSpeed;
		}

		boat.setVelocity(xVel, yVel, zVel);
	}
}
