package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;
import java.util.List;

public class Flight extends Module {

	private int antiKick = 0;

	public Flight() {
		super("Flight", ModuleCategory.MOVEMENT, GLFW.GLFW_KEY_F, new ModuleSettings(List.of(
				new SettingSlider(2, "Hspeed", 0.5, 10, 0.5),
				new SettingSlider(2,"Vspeed",0.5,5,0.5)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.hasVehicle()) return;

		double xVel = 0;
		double yVel = 0;
		double zVel = 0;

		double horizontalSpeed = (double) this.getSettings().getSetting(0).getValue() / 3;
		double verticalSpeed = (double) this.getSettings().getSetting(1).getValue() / 3;

		if (mc.options.jumpKey.isPressed()) {
			yVel = verticalSpeed;
		} else if (mc.options.sneakKey.isPressed()) {
			yVel = -verticalSpeed;
		}

		float yawRad = mc.player.getYaw() * MathHelper.RADIANS_PER_DEGREE;

		float sideways = mc.player.input.movementSideways;
		float forwards = mc.player.input.movementForward;

		if (!(sideways == 0 && forwards == 0))  {
			float moveAngle = (float) Math.atan2(sideways, forwards);

			xVel = MathHelper.sin(-yawRad + moveAngle) * horizontalSpeed;
			zVel = MathHelper.cos(-yawRad + moveAngle) * horizontalSpeed;
		}

		mc.player.setVelocity(xVel, yVel, zVel);

		if (antiKick % 2 == 0) {
			mc.player.setVelocity(mc.player.getVelocity().add(0, -0.08, 0));
		} else {
			mc.player.setVelocity(mc.player.getVelocity().add(0, 0.08, 0));
		}

		antiKick++;
	}
}
