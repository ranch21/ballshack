package org.ranch.ballshack.setting;

import org.ranch.ballshack.setting.settings.SettingBind;

import java.util.ArrayList;
import java.util.List;

public class ModuleSettings {

	private List<ModuleSetting<?>> settings;
	private SettingBind bind = new SettingBind(0, "KeyBind");

	public ModuleSettings(List<ModuleSetting<?>> settings) {
		this.settings = settings;
	}

	public List<ModuleSetting<?>> getSettings() {
		List<ModuleSetting<?>> merged = new ArrayList<>(settings);
		merged.add(bind);
		return new ArrayList<>(merged);
	}

	public SettingBind getBind() {
		return bind;
	}

	public ModuleSetting<?> getSetting(String name) {
		for (ModuleSetting<?> setting : settings) {
			if (setting.getName().equals(name)) {
				return setting;
			}
		}
		return null;
	}

	public ModuleSetting<?> getSetting(Class<?> clazz) {
		for (ModuleSetting<?> setting : settings) {
			if (setting.getClass().equals(clazz)) {
				return setting;
			}
		}
		return null;
	}

	public ModuleSetting<?> getSetting(int index) {
		return settings.get(index);
	}
}
