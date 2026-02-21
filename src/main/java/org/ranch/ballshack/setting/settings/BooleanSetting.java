package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonObject;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.CheckboxWidget;
import org.ranch.ballshack.setting.ModuleSetting;

public class BooleanSetting extends ModuleSetting<Boolean, BooleanSetting> {

	public BooleanSetting(String name, boolean value) {
		super(name, value);
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return new CheckboxWidget(getName(), x, y, height, height, this);
	}

	@Override
	public String getFormattedValue() {
		return value ? "True" : "False";
	}

	@Override
	public JsonObject getJson() {
		return null;
	}

	@Override
	public void readJson(JsonObject jsonObject) {

	}
}
