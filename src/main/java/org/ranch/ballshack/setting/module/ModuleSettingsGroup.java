package org.ranch.ballshack.setting.module;

import java.util.ArrayList;
import java.util.List;

public class ModuleSettingsGroup {

	protected final List<ModuleSetting<?, ?>> settings = new ArrayList<>();
	public final String name;

	public ModuleSettingsGroup(String name) {
		this.name = name;
	}

	public <T extends ModuleSetting<?, ?>> T add(T setting) {
		settings.add(setting);
		return setting;
	}

	public List<ModuleSetting<?, ?>> getSettings() {
		return settings;
	}
}
