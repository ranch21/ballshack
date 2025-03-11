package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.Arrays;

public class Speed extends Module {

	private int stage;
	private double speed, distance;

	public Speed() {
		super("Speed", ModuleCategory.MOVEMENT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(1, "Speed", 0.25, 4, 0.25)
		)));
	}

	@Override
	public void onEnable() {
		super.onEnable();
		stage = 0;
		speed = 0.2873;
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		distance = Math.sqrt((mc.player.getX() - mc.player.prevX) * (mc.player.getX() - mc.player.prevX) + (mc.player.getZ() - mc.player.prevZ) * (mc.player.getZ() - mc.player.prevZ));

		double speedM = (Double) getSettings().getSetting(0).getValue();

		switch (stage) {
			case 0:
				if (PlayerUtil.isMoving()) {
					stage++;
					speed = PlayerUtil.getSpeed();
				}
				//BallsLogger.info(String.valueOf(PlayerUtil.isMoving()));
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
