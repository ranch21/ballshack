package org.ranch.ballshack.setting;

import java.util.List;

public interface SettingsList {
	<T extends ModuleSetting<?>> T add(T setting);

	List<ModuleSetting<?>> getSettings();
}
