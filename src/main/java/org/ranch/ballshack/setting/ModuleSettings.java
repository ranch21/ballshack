package org.ranch.ballshack.setting;

import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingBind;

import java.util.ArrayList;
import java.util.List;

public class ModuleSettings implements SettingsList {

	protected final ModuleSettingsGroup defaultGroup = new ModuleSettingsGroup("default");
	protected final SettingBind bind;

	public ModuleSettings() {
		bind = defaultGroup.add(new SettingBind("KeyBind", 0));
	}

	public ModuleSettingsGroup getDefaultGroup() {
		return defaultGroup;
	}

	@Override
	public <T extends ModuleSetting<?, ?>> T add(T setting) {
		return defaultGroup.add(setting);
	}

	@Override
	public List<ModuleSetting<?, ?>> getSettings() {
		return defaultGroup.getSettings();
	}

	protected List<ModuleSetting<?, ?>> unpackSettings(List<ModuleSetting<?, ?>> settings) {
		List<ModuleSetting<?, ?>> list = new ArrayList<>();
		for (ModuleSetting<?, ?> setting : settings) {
			list.add(setting);
			if (setting instanceof SettingsList) {
				list.addAll(unpackSettings(((DropDown) setting).getSettings()));
			}
		}
		return list;
	}

	public SettingBind getBind() {
		return bind;
	}
}
