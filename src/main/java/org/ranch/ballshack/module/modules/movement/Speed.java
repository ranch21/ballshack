package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.PlayerUtil;

public class Speed extends Module {

	private int stage;
	private double speed;

	public final SettingSlider speedSlider = dGroup.add(new SettingSlider("Speed", 1, 0.25, 4, 0.25));

	public Speed() {
		super("Speed", ModuleCategory.MOVEMENT, 0, "go fast(er)");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		stage = 0;
		speed = 0.2873;
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		if (mc.player.isTouchingWater() ||
				mc.player.getAbilities().flying) return;

		double distance = Math.sqrt((mc.player.getX() - mc.player.lastX) * (mc.player.getX() - mc.player.lastX) + (mc.player.getZ() - mc.player.lastZ) * (mc.player.getZ() - mc.player.lastZ));

		double speedM = speedSlider.getValue();

		switch (stage) {
			case 0:
				if (PlayerUtil.isMoving()) {
					stage++;
					speed = PlayerUtil.getSpeed();
				}
				break;
			case 1:
				if (!PlayerUtil.isMoving() || !mc.player.isOnGround()) break;

				mc.player.setVelocity(mc.player.getVelocity().x, 0.4, mc.player.getVelocity().z);

				speed *= speedM;
				stage++;
				break;
			case 2:
				speed = distance - 0.76 * (distance - PlayerUtil.getSpeed());
				stage++;
				break;
			case 3:
				if (!mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, mc.player.getVelocity().y, 0.0)) || mc.player.verticalCollision) {
					stage = 0;
				}
				speed = distance - (distance / 159.0);
				break;
		}

		Vec3d movementVec = PlayerUtil.getMovementVector(speed, 0);

		//event.movement = new Vec3d(movementVec.x, event.movement.y, movementVec.z);
		mc.player.setVelocity(new Vec3d(movementVec.x, mc.player.getVelocity().y, movementVec.z));
	}
}
