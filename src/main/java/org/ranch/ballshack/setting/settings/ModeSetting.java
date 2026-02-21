package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonObject;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.ModeWidget;
import org.ranch.ballshack.setting.ModuleSetting;

public class ModeSetting<E extends Enum<?>> extends ModuleSetting<E, ModeSetting<E>> {

	private final E[] enumValues;

	public ModeSetting(String name, E defualt, E[] enumValues) {
		super(name, defualt);
		this.enumValues = enumValues;
	}

	public E[] getEnumValues() {
		return enumValues;
	}

	public int getValueInt() {
		return getValue().ordinal();
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return new ModeWidget(getName(), x, y, width, height, this);
	}

	@Override
	public String getFormattedValue() {
		return getValue().name();
	}

	@Override
	public JsonObject getJson() {
		return null;
	}

	@Override
	public void readJson(JsonObject jsonObject) {

	}
}
