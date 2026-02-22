package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.settings.NumberSetting;

public class Rainbow extends Module {

	public final NumberSetting saturation = dGroup.add(new NumberSetting("Sat", 0.5).min(0).max(1).step(0.05));
	public final NumberSetting brightness = dGroup.add(new NumberSetting("Bri", 1).min(0).max(1).step(0.05));
	public final NumberSetting speed = dGroup.add(new NumberSetting("Speed", 1).min(0).max(5).step(0.5));

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