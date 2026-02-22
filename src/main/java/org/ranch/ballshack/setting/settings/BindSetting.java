package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.BindWidget;
import org.ranch.ballshack.setting.ModuleSetting;

public class BindSetting extends ModuleSetting<Integer, BindSetting> {

	public static final int NONE = 0;

	public BindSetting(String name, int value) {
		super(name, value);
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return new BindWidget(getName(), x, y, width, height, this);
	}

	@Override
	public String getFormattedValue() {
		return String.valueOf(getValue());
	}

	@Override
	public JsonElement getJson() {
		return new JsonPrimitive(getValue());
	}

	@Override
	public void readJson(JsonElement jsonElement) {
		setValue(jsonElement.getAsInt());
	}
}
