package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventSetSneaking;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.util.PlayerUtil;

import static org.ranch.ballshack.util.EntityUtil.getMaxAllowedFloatingTicks;

public class Flight extends Module {

	private int antiKick = 0;

	public final NumberSetting hSpeed = dGroup.add(new NumberSetting("Hspeed", 2).min(0.5).max(10).step(0.5).featured());
	public final NumberSetting vSpeed = dGroup.add(new NumberSetting("Vspeed", 2).min(0.5).max(5).step(0.5));

	public Flight() {
		super("Flight", ModuleCategory.MOVEMENT, 0, "hawk tuah man!");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.hasVehicle()) return;

		double horizontalSpeed = hSpeed.getValue() / 3;
		double verticalSpeed = vSpeed.getValue() / 3;

		Vec3d movementVec = PlayerUtil.getMovementVector(horizontalSpeed, verticalSpeed);

		antiKick++;
		if (antiKick > getMaxAllowedFloatingTicks(mc.player) - 3) {
			if (antiKick > getMaxAllowedFloatingTicks(mc.player))
				antiKick = 0;
			movementVec = new Vec3d(movementVec.x, -0.05, movementVec.z);
		} else if (antiKick == 1) {
			movementVec = new Vec3d(movementVec.x, 0.05 * 3, movementVec.z);
		}
		mc.player.setVelocity(movementVec);

	}

	@EventSubscribe
	public void onSneak(EventSetSneaking event) {
		event.cancel();
	}
}
