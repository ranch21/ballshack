package org.ranch.ballshack.gui.windows.widgets.setting;

import org.ranch.ballshack.gui.windows.widgets.PressableWidget;
import org.ranch.ballshack.setting.ISetting;

public abstract class SettingWidget<T> extends PressableWidget {

	protected ISetting<T> setting;

	public SettingWidget(String title, int x, int y, int width, int height, ISetting<T> setting) {
		super(title, x, y, width, height);
		this.setting = setting;
	}
}
