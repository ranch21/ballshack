package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

public class Rainbow extends Module {

	public SettingSlider saturation = dGroup.add(new SettingSlider(0.5, "Sat", 0, 1, 0.05));
	public SettingSlider brightness = dGroup.add(new SettingSlider(1, "Bri", 0, 1, 0.05));
	public SettingSlider speed = dGroup.add(new SettingSlider(1, "Speed", 0, 5, 0.5));

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
