package org.ranch.ballshack.setting;

import java.util.List;

public class ModuleSettings {

	private List<ModuleSetting> settings;

	public ModuleSettings(List<ModuleSetting> settings) {
		this.settings = settings;
	}

	public List<ModuleSetting> getSettings() {
		return settings;
	}

	public ModuleSetting getSetting(int index) {
		return settings.get(index);
	}
}
