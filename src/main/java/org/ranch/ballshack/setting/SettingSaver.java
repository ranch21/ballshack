package org.ranch.ballshack.setting;

import com.google.gson.*;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.Constants;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingHud;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingSaver {

	private static ScheduledExecutorService scheduler;

	public static final AtomicBoolean SCHEDULE_SAVE = new AtomicBoolean();

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Path saveDir;

	public static void startExecutor() {
		if (scheduler == null)
			scheduler = new ScheduledThreadPoolExecutor(1);

		//if (SCHEDULE_SAVE.getAndSet(false)) saveSettings();
		scheduler.scheduleAtFixedRate(SettingSaver::saveSettings, 0, 5, TimeUnit.SECONDS);
	}

	public static void init() {
		saveDir = BallsHack.getSaveDir();
		if (!Files.exists(saveDir)) {
			try {
				Files.createDirectories(saveDir);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		startExecutor();
	}

	private static JsonObject getSettings(List<ModuleSetting<?, ?>> settings) {
		JsonObject settingsJson = new JsonObject();
		for (ModuleSetting<?, ?> setting : settings) {
			Object value = setting.getValue();

			if (setting instanceof DropDown) {
				settingsJson.add(setting.getName(), getSettings(((DropDown) setting).getSettings()));
				continue;
			}

			if (setting instanceof SettingHud hsetting) {
				settingsJson.addProperty("x", hsetting.value.x);
				settingsJson.addProperty("y", hsetting.value.y);
				settingsJson.addProperty("anchor", hsetting.value.anchor.ordinal());
				continue;
			}

			settingsJson.add(setting.getName(), setting.getJson());
		}
		return settingsJson;
	}

	public static JsonObject saveModules() {
		JsonObject modulesJson = new JsonObject();

		for (Module mod : ModuleManager.getModules()) {
			JsonObject modjson = new JsonObject();

			if (mod.isEnabled() && !mod.isMeta()) {
				modjson.addProperty("enabled", mod.isEnabled());
			}

			JsonObject settingsJson = getSettings(mod.getSettings().getSettings());

			if (!settingsJson.isEmpty())
				modjson.add("settings", settingsJson);

			if (!modjson.isEmpty())
				modulesJson.add(mod.getName(), modjson);
		}

		return modulesJson;
	}

	public static void saveSettings() {
		JsonObject json = new JsonObject();

		JsonObject modulesJson = saveModules();

		JsonObject settingsJson = SettingsManager.getJson();

		json.addProperty("version", Constants.SETTINGS_FORMAT_VERSION);
		json.add("settings", settingsJson);
		json.add("modules", modulesJson);

		try (Writer writer = new FileWriter(saveDir.resolve("settings.json").toFile())) {
			gson.toJson(json, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadModules(JsonObject modules) {
		for (String modName : modules.keySet()) {
			JsonObject modjson = modules.getAsJsonObject(modName);
			Module mod = ModuleManager.getModuleByName(modName);

			if (mod == null) continue;

			if (modjson.has("enabled")) {
				mod.onEnable();
			}

			if (modjson.has("settings")) {
				JsonObject settingsJson = modjson.getAsJsonObject("settings");

				setSettings(settingsJson, mod);
			}
			BallsLogger.info("Loaded settings for " + modName);
		}
	}

	private static void setSettings(JsonObject settingsJson, Module mod) {
		BallsLogger.info(settingsJson.toString());
		for (ModuleSetting<?, ?> setting : mod.getSettings().getSettings()) {
			String settingName = setting.getName();

			if (settingsJson.has(settingName)) {
				JsonObject element = settingsJson.getAsJsonObject(settingName);
				setting.readJson(element);
			}
		}

		if (mod instanceof ModuleHud hudMod && settingsJson.has("x")) {
			int x = settingsJson.get("x").getAsInt();
			int y = settingsJson.get("y").getAsInt(); //goog goo ga gaa
			int anchor = settingsJson.get("anchor").getAsInt();
			hudMod.setAnchorPoint(ModuleAnchor.values()[anchor]);
			hudMod.offsetx = x;
			hudMod.offsety = y;
		}
	}

	public static void readSettings() {
		if (!Files.exists(saveDir.resolve("settings.json"))) {
			BallsLogger.warn("No settings file found, skipping loading.");
			return;
		}

		try (FileReader reader = new FileReader(saveDir.resolve("settings.json").toFile())) {

			JsonObject settings = JsonParser.parseReader(reader).getAsJsonObject();
			JsonElement settings_format_version = settings.get("version");
			if (settings_format_version == null || settings_format_version.getAsInt() != Constants.SETTINGS_FORMAT_VERSION) {
				BallsLogger.warn("Incompatible settings file format, creating backup and skipping loading.");
				Files.copy(saveDir.resolve("settings.json"), saveDir.resolve("settings_backup.json"), StandardCopyOption.REPLACE_EXISTING);
				return;
			}
			JsonObject config = settings.get("settings").getAsJsonObject();
			JsonObject modules = settings.get("modules").getAsJsonObject();

			loadModules(modules);
			SettingsManager.loadSettings(config);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Object getTypedValue(JsonElement element, Object defaultValue) {
		if (defaultValue instanceof String) {
			return element.getAsString();
		} else if (defaultValue instanceof Integer) {
			return element.getAsInt();
		} else if (defaultValue instanceof Double) {
			return element.getAsDouble();
		} else if (defaultValue instanceof Float) {
			return element.getAsFloat();
		} else if (defaultValue instanceof Long) {
			return element.getAsLong();
		} else if (defaultValue instanceof Boolean) {
			return element.getAsBoolean();
		}
		return null;
	}

}
