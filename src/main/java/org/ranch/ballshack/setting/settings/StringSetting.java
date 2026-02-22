package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.StringWidget;
import org.ranch.ballshack.setting.ModuleSetting;

public class StringSetting extends ModuleSetting<String, StringSetting> {

	private int maxLength = 256;

	public StringSetting(String name, String value) {
		super(name, value);
	}

	public StringSetting maxLen(int maxLen) {
		this.maxLength = maxLen;
		return this;
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return new StringWidget(getName(), x, y, width, height, this);
	}

	@Override
	public String getFormattedValue() {
		return value;
	}

	@Override
	public JsonElement getJson() {
		return new JsonPrimitive(getValue());
	}

	@Override
	public void readJson(JsonElement jsonElement) {
		setValue(jsonElement.getAsString());
	}
}
