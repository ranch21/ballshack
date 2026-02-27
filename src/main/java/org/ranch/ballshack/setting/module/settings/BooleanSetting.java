package org.ranch.ballshack.setting.module.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.CheckboxWidget;
import org.ranch.ballshack.setting.module.ModuleSetting;

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
	public JsonElement getJson() {
		return new JsonPrimitive(getValue());
	}

	@Override
	public void readJson(JsonElement jsonElement) {
		setValue(jsonElement.getAsBoolean());
	}
}
