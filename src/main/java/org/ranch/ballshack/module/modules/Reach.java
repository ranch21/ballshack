package org.ranch.ballshack.module.modules;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventReach;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;

import java.util.Arrays;

public class Reach extends Module {
	public Reach() {
		super("Reach", ModuleCategory.PLAYER, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(4.5, "Dist", 1, 8, 0.5)
		)));
	}

	@EventSubscribe
	public void onReach(EventReach event) {
		event.setReach(((Double) getSettings().getSetting(0).getValue()).floatValue());
	}
}
