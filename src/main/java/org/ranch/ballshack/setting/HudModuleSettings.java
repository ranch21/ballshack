package org.ranch.ballshack.setting;

import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.setting.moduleSettings.SettingHud;

import java.util.ArrayList;
import java.util.List;

public class HudModuleSettings extends ModuleSettings {

	private final SettingHud hsetting = defaultGroup.add(new SettingHud(new HudElementData(0, 0, ModuleAnchor.TOP_LEFT)));

	public HudModuleSettings() {
		super();
	}

	public SettingHud getHudSetting() {
		return hsetting;
	}
}
