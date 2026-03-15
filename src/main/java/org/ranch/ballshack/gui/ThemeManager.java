package org.ranch.ballshack.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.setting.client.ClientSetting;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class ThemeManager {

	public static final String folder = "themes/";

	public static final ClientSetting<Boolean> loaded = new ClientSetting<>("other.theme_loaded", false);

	private static JsonObject themeJson;

	public static void loadTheme(String name) {
		if (!Files.exists(BallsHack.getSaveDir().resolve(folder + name + ".json"))) {
			BallsLogger.warn("Theme file not found.");
			return;
		}

		try (FileReader reader = new FileReader(BallsHack.getSaveDir().resolve(folder + name + ".json").toFile())) {
			themeJson = JsonParser.parseReader(reader).getAsJsonObject();
			loaded.setValue(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void clearTheme() {
		themeJson = null;
		loaded.setValue(false);
	}

	public static Color getColor(String name) {
		if (themeJson == null || themeJson.get(name) == null || themeJson.get(name).isJsonNull())
			return null;
		JsonArray col = themeJson.get(name).getAsJsonArray();
		if (col == null || col.isJsonNull())
			return null;
		return new Color(col.get(0).getAsInt(), col.get(1).getAsInt(), col.get(2).getAsInt(), col.get(3).getAsInt());
	}

	public record SavedColor(Color defaultCol, String key) {
		public Color getColor() {
			Color loaded = ThemeManager.getColor(key());
			if (loaded == null)
				return defaultCol();
			else
				return loaded;
		}

		@Override
		public int hashCode() {
			throw new RuntimeException("No"); // hehe
		}
	}
}
