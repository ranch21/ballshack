package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.gui.ThemeManager;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingString;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

public class Themes extends Module {

	public final SettingString theme = dGroup.add(new SettingString("Theme", "default"));
	public final SettingToggle load = dGroup.add(new SettingToggle("Load", false) {
		@Override
		public void setValue(Boolean value) {
			ThemeManager.loadTheme(theme.getValue());
			this.value = false;
		}
	});
	public final SettingToggle unload = dGroup.add(new SettingToggle("Unload", false) {
		@Override
		public void setValue(Boolean value) {
			ThemeManager.clearTheme();
			this.value = false;
		}
	});

	public Themes() {
		super("Themes", ModuleCategory.CLIENT, 0, "set the colooorsss");
	}
}
