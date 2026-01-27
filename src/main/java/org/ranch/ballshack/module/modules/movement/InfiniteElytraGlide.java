package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.Rotation;
import org.ranch.ballshack.util.RotationUtil;

public class InfiniteElytraGlide extends Module {

	private double startingHeight;
	private double bottomHeight;

	private Rotation target;

	private int stage = 0;

	public final SettingSlider maxHeight = dGroup.add(new SettingSlider(200, "Max Height", 1, 1000, 1));
	public final SettingToggle instantTurn = dGroup.add(new SettingToggle(true, "Instant Turn"));
	public final SettingSlider turnSpeed = dGroup.add(new SettingSlider(1, "Turn Speed", 1, 10, 1));

	public InfiniteElytraGlide() {
		super("ElytraGlide", ModuleCategory.MOVEMENT, 0, "ion need sum hacks to do this");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.hasVehicle()) return;
		if (!mc.player.isGliding()) {
			reset();
			return;
		}

		target.yaw = mc.player.getYaw();

		switch (stage) {
			case 0:
				target.pitch = 32.5f;
				if (mc.player.getY() <= bottomHeight) stage = 1;
				break;
			case 1:
				target.pitch = -49;
				if (mc.player.getVelocity().y >= 0) {
					stage = 2;
				}
				break;
			case 2:
				target.pitch = target.pitch + 0.5f;
				if (mc.player.getVelocity().y <= 0 || mc.player.getY() > maxHeight.getValue()) {
					startingHeight = mc.player.getY();
					bottomHeight = startingHeight - 50;
					stage = 0;
				}
				break;
		}

		if (instantTurn.getValue()) {
			mc.player.setPitch(target.pitch);
		}
	}

	@EventSubscribe
	public void onMouseUpdate(EventMouseUpdate event) {

		if (mc.player.hasVehicle() || instantTurn.getValue()) return;
		if (!mc.player.isGliding()) {
			reset();
			return;
		}

		Rotation step = RotationUtil.slowlyTurnTowards(target, (float) (double) turnSpeed.getValue());

		event.deltaX = (step.yaw - mc.player.getYaw()) + event.origDeltaX;
		event.deltaY = (step.pitch - mc.player.getPitch()) + event.origDeltaY;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (mc.player == null) return;
		reset();
	}

	private void reset() {
		target = new Rotation(mc.player.getYaw(), 0);
		stage = 0;
		startingHeight = mc.player.getY();
		bottomHeight = startingHeight - 50;
		//BallsLogger.info(String.valueOf(startingHeight));
	}
}
