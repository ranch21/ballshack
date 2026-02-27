package org.ranch.ballshack.setting.module.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.setting.module.HudElementData;
import org.ranch.ballshack.setting.module.ModuleSetting;

public class HudSetting extends ModuleSetting<HudElementData, HudSetting> {

	public HudSetting(String name, HudElementData value) {
		super(name, value);
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return null;
	}

	@Override
	public String getFormattedValue() {
		return String.valueOf(getValue());
	}

	@Override
	public JsonElement getJson() {
		JsonObject object = new JsonObject();
		object.addProperty("x", getValue().x);
		object.addProperty("y", getValue().y);
		object.addProperty("anchor", getValue().anchor.ordinal());
		return object;
	}

	@Override
	public void readJson(JsonElement jsonElement) {
		int x = jsonElement.getAsJsonObject().get("x").getAsInt();
		int y = jsonElement.getAsJsonObject().get("y").getAsInt();
		int anchor = jsonElement.getAsJsonObject().get("anchor").getAsInt();
		setValue(new HudElementData(x, y, ModuleAnchor.values()[anchor]));
	}
}
