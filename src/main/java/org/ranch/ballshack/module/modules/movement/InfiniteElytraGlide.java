package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

import java.util.Arrays;

public class InfiniteElytraGlide extends Module {

	private double startingHeight;
	private double bottomHeight;



	private int stage = 0;

	public InfiniteElytraGlide() {
		super("ElytraGlide", ModuleCategory.MOVEMENT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(200, "Max Height", 1, 1000, 1)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.hasVehicle()) return;
		if (!mc.player.isGliding()) {
			reset();
			return;
		}

		//BallsLogger.info(String.valueOf(mc.player.getY() <= bottomHeight));
		BallsLogger.info("tick");


		switch (stage) {
			case 0:
				BallsLogger.info("0");
				mc.player.setPitch(32.5f);
				if (mc.player.getY() <= bottomHeight) stage = 1;
				break;
			case 1:
				BallsLogger.info("1");
				mc.player.setPitch(-49);
				if (mc.player.getVelocity().y >= 0) {
					stage = 2;
				}
				break;
			case 2:
				BallsLogger.info("2");
				mc.player.setPitch(mc.player.getPitch() + 0.5f);
				if (mc.player.getVelocity().y <= 0 || mc.player.getY() > (double) settings.getSetting(0).getValue()) {
					startingHeight = mc.player.getY();
					bottomHeight = startingHeight - 50;
					stage = 0;
				}
				break;
		}

	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (mc.player == null) return;
		reset();
	}

	private void reset() {
		stage = 0;
		startingHeight = mc.player.getY();
		bottomHeight = startingHeight - 50;
		//BallsLogger.info(String.valueOf(startingHeight));
	}
}
