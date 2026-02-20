package org.ranch.ballshack.gui.clickgui;

import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.TextWidget;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.settings.BooleanSetting;

import java.util.List;

public class ModuleSettingWindow extends Window {

	public ModuleSettingWindow(ModuleSettingsGroup settings, int x, int y, int width, int height) {
		super(settings.name, x, y, width, height);

		int i = 0;
		for (ModuleSetting<?, ?> setting : settings.getSettings()) {
			addWidget(setting, i++);
		}
	}

	private void addWidget(ModuleSetting<?, ?> setting, int index) {
		int y = index * 15 + 5;

		addChild(new TextWidget(
				setting.getName(),
				2, y,
				10, 10
		));

		addChild(setting.getWidget(
				mc.textRenderer.getWidth(setting.getName()) + 4,
				y,
				getWidth() - mc.textRenderer.getWidth(setting.getName()) - 8, 10
		));
	}
}
