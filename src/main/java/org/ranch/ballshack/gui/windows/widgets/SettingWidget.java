package org.ranch.ballshack.gui.windows.widgets;

import org.ranch.ballshack.setting.ISetting;

public class SettingWidget<T> extends Widget {

	protected ISetting<T> setting;

	public SettingWidget(String title, int x, int y, int width, int height, ISetting<T> setting) {
		super(title, x, y, width, height);
		this.setting = setting;
	}
}
