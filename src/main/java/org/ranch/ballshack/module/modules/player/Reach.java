package org.ranch.ballshack.module.modules.player;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventReach;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

import java.util.Collections;

public class Reach extends Module {
	public Reach() {
		super("Reach", ModuleCategory.PLAYER, 0, new ModuleSettings(Collections.singletonList(
				new SettingSlider(4.5, "Dist", 1, 8, 0.5).featured()
		)));
	}

	@EventSubscribe
	public void onReach(EventReach event) {
		event.reach = ((Double) getSettings().getSetting(0).getValue()).floatValue();
	}
}
