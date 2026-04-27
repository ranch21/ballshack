package org.ranch.ballshack.setting.module;

import org.ranch.ballshack.setting.module.settings.*;

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

	/* BEGIN CONVENIENCE FUNCTIONS */

	public BooleanSetting bool(String name, boolean value) {
		return add(new BooleanSetting(name, value));
	}

	public BooleanSetting bool(String name) {
		return add(new BooleanSetting(name, false));
	}

	public NumberSetting num(String name, double value) {
		return add(new NumberSetting(name, value));
	}

	public <E extends Enum<?>> ModeSetting<E> mode(String name, E value, E[] values) {
		return add(new ModeSetting<>(name, value, values));
	}

	public StringSetting str(String name, String value) {
		return add(new StringSetting(name, value));
	}

	public BlocksSetting blocks(String name) {
		return add(new BlocksSetting(name));
	}

	public RotateModeSetting rotate(String name) {
		return add(new RotateModeSetting(name));
	}

	public SortModeSetting sort(String name) {
		return add(new SortModeSetting(name));
	}

}
