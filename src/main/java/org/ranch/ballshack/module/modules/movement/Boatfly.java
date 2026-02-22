package org.ranch.ballshack.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPlayerInput;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;

import static org.ranch.ballshack.util.EntityUtil.getMaxAllowedFloatingTicks;

public class Boatfly extends Module {

	private int antiKick = 0;

	public final NumberSetting hSpeed = dGroup.add(new NumberSetting("Hspeed", 2).min(1).max(10).step(0.5).featured());
	public final NumberSetting vSpeed = dGroup.add(new NumberSetting("Vspeed", 2).min(1).max(4).step(0.5));
	public final BooleanSetting antiSuffocate = dGroup.add(new BooleanSetting("AntiSuffocate", true));

	public Boatfly() {
		super("BoatFly", ModuleCategory.MOVEMENT, 0, "popboat (sprint to dismount)");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player == null || !mc.player.hasVehicle())
			return;

		Entity boat = mc.player.getVehicle();

		if (!(boat instanceof BoatEntity))
			return;

		double xVel = 0;
		double yVel = 0;
		double zVel = 0;

		double verticalSpeed = vSpeed.getValue() / 3;
		double horizontalSpeed = hSpeed.getValue() / 3;

		if (mc.options.jumpKey.isPressed()) {
			yVel = verticalSpeed;
		} else if (mc.options.sneakKey.isPressed()) {
			yVel = -verticalSpeed;
		}

		Vec2f input = new Vec2f(0, 0);

		if (mc.options.forwardKey.isPressed()) {
			input = input.add(new Vec2f(0, 1));
		}
		if (mc.options.backKey.isPressed()) {
			input = input.add(new Vec2f(0, -1));
		}
		if (mc.options.rightKey.isPressed()) {
			input = input.add(new Vec2f(1, 0));
		}
		if (mc.options.leftKey.isPressed()) {
			input = input.add(new Vec2f(-1, 0));
		}

		if (input.lengthSquared() > 0) {
			input = input.normalize();
			float yawRad = (float) ((boat.getYaw() * MathHelper.RADIANS_PER_DEGREE) + Math.atan2(input.x, input.y));

			xVel = MathHelper.sin(-yawRad) * horizontalSpeed;
			zVel = MathHelper.cos(yawRad) * horizontalSpeed;
		}

		boat.rotate(mc.player.headYaw, false, boat.getPitch(), false);

		if (mc.player.isInsideWall() && antiSuffocate.getValue()) {
			yVel = -1;
		}

		antiKick++;
		if (antiKick > getMaxAllowedFloatingTicks(mc.player) - 20) {
			antiKick = 0;
			boat.setVelocity(xVel, yVel + boat.getFinalGravity() - 0.06, zVel);
		} else if (antiKick == 1) {
			boat.setVelocity(xVel, yVel + boat.getFinalGravity() - 0.06, zVel);
		} else {
			boat.setVelocity(xVel, yVel + boat.getFinalGravity(), zVel);
		}
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerInput event) {
		if (mc.player == null || !mc.player.hasVehicle())
			return;

		Entity boat = mc.player.getVehicle();

		if (!(boat instanceof BoatEntity))
			return;

		event.forward = false;
		event.backward = false;
		event.left = false;
		event.right = false;
		event.jump = false;
		event.sneak = event.origSprint;
		event.sprint = false;
	}
}
