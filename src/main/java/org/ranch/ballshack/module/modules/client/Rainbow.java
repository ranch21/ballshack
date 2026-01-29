package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

public class Rainbow extends Module {

	public final SettingSlider saturation = dGroup.add(new SettingSlider("Sat", 0.5, 0, 1, 0.05));
	public final SettingSlider brightness = dGroup.add(new SettingSlider("Bri", 1, 0, 1, 0.05));
	public final SettingSlider speed = dGroup.add(new SettingSlider("Speed", 1, 0, 5, 0.5));

	public Rainbow() {
		super("Rainbow", ModuleCategory.CLIENT, 0, "Taste the rainbow", true);
		onEnable();
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		Colors.globalRainbowSaturation = (float) (double) saturation.getValue();
		Colors.globalRainbowBrightness = (float) (double) brightness.getValue();
		Colors.globalRainbowSpeed = (float) (double) speed.getValue();
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
