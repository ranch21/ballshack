package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.gui.ThemeManager;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettingSaver;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.StringSetting;

public class Themes extends Module {

	public final StringSetting theme = dGroup.add(new StringSetting("Theme", "default"));
	public final BooleanSetting load = dGroup.add(new BooleanSetting("Load", false) {
		@Override
		public void setValue(Boolean value) {
			ThemeManager.loadTheme(theme.getValue());
			ModuleSettingSaver.markDirty();
			this.value = false;
		}
	});
	public final BooleanSetting unload = dGroup.add(new BooleanSetting("Unload", false) {
		@Override
		public void setValue(Boolean value) {
			ThemeManager.clearTheme();
			ModuleSettingSaver.markDirty();
			this.value = false;
		}
	});

	public Themes() {
		super("Themes", ModuleCategory.CLIENT, 0, "set the colooorsss");
	}
}
