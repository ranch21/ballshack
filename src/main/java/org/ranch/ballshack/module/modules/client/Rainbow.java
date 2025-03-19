package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

import java.util.Arrays;

public class Rainbow extends Module {
	public Rainbow() {
		super("Rainbow", ModuleCategory.CLIENT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(0.5, "Sat", 0, 1, 0.05),
				new SettingSlider(1, "Bri", 0, 1, 0.05),
				new SettingSlider(1, "Speed", 0, 5, 0.5)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		double sat = (double) settings.getSetting(0).getValue();
		double bri = (double) settings.getSetting(1).getValue();
		double speed = (double) settings.getSetting(2).getValue();

		Colors.globalRainbowSaturation = (float) sat;
		Colors.globalRainbowBrightness = (float) bri;
		Colors.globalRainbowSpeed = (float) speed;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
