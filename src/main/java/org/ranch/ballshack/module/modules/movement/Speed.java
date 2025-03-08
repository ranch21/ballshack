package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.MathHelper;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;

import java.util.Arrays;

public class Speed extends Module {
	public Speed() {
		super("Speed", ModuleCategory.MOVEMENT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(1, "Speed", 0.5, 4, 0.01)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.hasVehicle()) return;

		double xVel = 0;
		double yVel = mc.player.getVelocity().y;
		double zVel = 0;

		double horizontalSpeed = (double) this.getSettings().getSetting(0).getValue() / 3;

		float yawRad = mc.player.getYaw() * MathHelper.RADIANS_PER_DEGREE;

		float sideways = mc.player.input.movementSideways;
		float forwards = mc.player.input.movementForward;

		if (!(sideways == 0 && forwards == 0))  {
			float moveAngle = (float) Math.atan2(sideways, forwards);

			xVel = MathHelper.sin(-yawRad + moveAngle) * horizontalSpeed;
			zVel = MathHelper.cos(-yawRad + moveAngle) * horizontalSpeed;
		}

		mc.player.setVelocity(xVel, yVel, zVel);
	}
}
