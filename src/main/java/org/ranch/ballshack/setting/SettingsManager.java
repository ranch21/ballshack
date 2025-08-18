package org.ranch.ballshack.setting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsManager {
	private static final Map<String, Setting<?>> settings = new HashMap<>();
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static void registerSetting(Setting<?> setting) {
		settings.put(setting.getKey(), setting);
	}

	public static void loadSettings(JsonObject json) {
		for (Map.Entry<String, JsonElement> jsonEntry : json.entrySet()) {
			Setting<?> setting = settings.get(jsonEntry.getKey());
			if (setting != null) {
				Object value;
				if (setting.getValue() instanceof String) {
					value = jsonEntry.getValue().getAsString();
				} else if (setting.getValue() instanceof Boolean) {
					value = jsonEntry.getValue().getAsBoolean();
				} else if (setting.getValue() instanceof Number) {
					value = jsonEntry.getValue().getAsNumber();
				} else {
					value = gson.fromJson(jsonEntry.getValue(), setting.getType());
				}
				setSettingValue(setting, value);
			}
		}
	}

	public static JsonObject getJson() {
		JsonObject json = new JsonObject();
		for (Map.Entry<String, Setting<?>> setting : settings.entrySet()) {
			if (setting.getValue().getValue() instanceof String) {
				json.addProperty(setting.getKey(), (String) setting.getValue().getValue());
			} else if (setting.getValue().getValue() instanceof Boolean) {
				json.addProperty(setting.getKey(), (Boolean) setting.getValue().getValue());
			} else if (setting.getValue().getValue() instanceof Number) {
				json.addProperty(setting.getKey(), (Number) setting.getValue().getValue());
			} else {
				json.add(setting.getKey(), gson.toJsonTree(setting.getValue().getValue()));
			}

		}
		return json;
	}

	private static <T> void setSettingValue(Setting<T> setting, Object value) {
		setting.setValue((T) value);
	}
}
