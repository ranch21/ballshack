package org.ranch.ballshack.setting;

import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingBind;

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

	private List<ModuleSetting<?>> unpackSettings(List<ModuleSetting<?>> settings) {
		List<ModuleSetting<?>> list = new ArrayList<>();
		for (ModuleSetting<?> setting : settings) {
			list.add(setting);
			if (setting instanceof DropDown) {
				list.addAll(unpackSettings(((DropDown) setting).getSettings()));
			}
		}
		return list;
	}

	public List<ModuleSetting<?>> getSettingsUnpacked() {
		List<ModuleSetting<?>> merged = new ArrayList<>(unpackSettings(settings));
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
