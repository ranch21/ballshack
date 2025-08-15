package org.ranch.ballshack.setting;

import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.setting.moduleSettings.SettingHud;

import java.util.ArrayList;
import java.util.List;

public class HudModuleSettings extends ModuleSettings {

	private SettingHud hsetting = new SettingHud(new HudElementData(0,0, ModuleAnchor.TOP_LEFT));

	public HudModuleSettings(List<ModuleSetting<?>> settings) {
		super(settings);
	}

	@Override
	public List<ModuleSetting<?>> getSettings() {
		List<ModuleSetting<?>> merged = new ArrayList<>(settings);
		merged.add(hsetting);
		merged.add(bind);
		return new ArrayList<>(merged);
	}

	@Override
	public List<ModuleSetting<?>> getSettingsUnpacked() {
		List<ModuleSetting<?>> merged = new ArrayList<>(unpackSettings(settings));
		merged.add(hsetting);

		merged.add(bind);
		return new ArrayList<>(merged);
	}

	public SettingHud getHudSetting() {
		return hsetting;
	}
}
