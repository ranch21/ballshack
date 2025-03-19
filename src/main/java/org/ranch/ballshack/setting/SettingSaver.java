package org.ranch.ballshack.setting;

import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.command.commands.GPTCommand;
import org.ranch.ballshack.gui.WindowScreen;
import org.ranch.ballshack.gui.window.widgets.TextFieldWidget;
import org.ranch.ballshack.gui.window.windows.SettingsWindow;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.settings.DropDown;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingSaver {

	private static ScheduledExecutorService scheduler;

	public static AtomicBoolean SCHEDULE_SAVE = new AtomicBoolean();

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static Path saveDir;

	public static void startExecutor() {
		if (scheduler == null)
			scheduler = new ScheduledThreadPoolExecutor(1);

		scheduler.scheduleAtFixedRate(() -> {
			if (SCHEDULE_SAVE.getAndSet(false)) saveSettings();
		}, 0, 5, TimeUnit.SECONDS);
	}

	public static void init() {
		saveDir = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "ballshack/");
		if (!Files.exists(saveDir)) {
			try {
				Files.createDirectories(saveDir);
			} catch (IOException e) {
				e.printStackTrace();
				return; // Prevent further execution if directory creation fails
			}
		}
		startExecutor();
	}

	private static JsonObject getSettings(List<ModuleSetting<?>> settings) {
		JsonObject settingsJson = new JsonObject();
		for (ModuleSetting<?> setting : settings) {
			Object value = setting.getValue(); // Capture the generic value

			if (setting instanceof DropDown) {
				settingsJson.add(setting.getName(), getSettings(((DropDown) setting).getSettings()));
				continue;
			}

			if (value instanceof String) {
				settingsJson.addProperty(setting.getName(), (String) value);
			} else if (value instanceof Number) {
				settingsJson.addProperty(setting.getName(), (Number) value);
			} else if (value instanceof Boolean) {
				settingsJson.addProperty(setting.getName(), (Boolean) value);
			} else {
				settingsJson.addProperty(setting.getName(), value.toString()); // Fallback to string
			}
		}
		return settingsJson;
	}

	private static JsonObject saveCommands() {
		JsonObject commands = new JsonObject();
		commands.addProperty("api-key", GPTCommand.api_key);
		commands.addProperty("prefix", CommandManager.prefix);
		return commands;
	}

	private static void loadCommands(JsonObject settings) {
		JsonObject gpt = settings.get("commands").getAsJsonObject();
		GPTCommand.api_key = gpt.getAsJsonPrimitive("api-key").getAsString();
		CommandManager.prefix = gpt.getAsJsonPrimitive("prefix").getAsString();
	}

	private static JsonObject saveFriends() {
		JsonObject friends = new JsonObject();
		friends.add("friends", gson.toJsonTree(FriendManager.getFriends()));
		return friends;
	}

	private static void loadFriends(JsonObject settings) {
		JsonObject friends = settings.get("friends").getAsJsonObject();
		JsonArray array = friends.get("friends").getAsJsonArray();
		List<String> list = new ArrayList<>();
		for (JsonElement friend : array) {
			FriendManager.add(friend.getAsString());
		}
	}

	public static void saveSettings() {
		JsonObject json = new JsonObject();

		json.addProperty("watermark", ((TextFieldWidget) WindowScreen.getWindow(SettingsWindow.class).widgets.get(0)).getText());

		JsonObject commands = saveCommands();
		json.add("commands", commands);
		JsonObject friends = saveFriends();
		json.add("friends", friends);
		JsonObject modulesJson = new JsonObject();

		for (Module mod: ModuleManager.getModules()) {
			JsonObject modjson = new JsonObject();

			if (mod.isEnabled() && !mod.getName().equals("ClickGui") && !mod.getName().equals("WinGui")) {
				modjson.addProperty("enabled", mod.isEnabled());
			}

			JsonObject settingsJson = getSettings(mod.getSettings().getSettings());

			if (settingsJson.size() != 0)
				modjson.add("settings", settingsJson);

			if (modjson.size() != 0)
				modulesJson.add(mod.getName(), modjson);
		}

		json.add("modules", modulesJson);

		try (Writer writer = new FileWriter(saveDir.resolve("settings.json").toFile())) {
			gson.toJson(json, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setSettings(JsonObject settingsJson, Module mod) {
		BallsLogger.info(settingsJson.toString());
		for (ModuleSetting<?> setting : mod.getSettings().getSettingsUnpacked()) {
			String settingName = setting.getName();

			if (settingsJson.has(settingName)) {
				JsonElement element = settingsJson.get(settingName);

				//BallsLogger.info(element.toString());

				if (element.isJsonObject()) {
					BallsLogger.info(element.getAsJsonObject().toString());
					setSettings(element.getAsJsonObject(), mod);
					continue;
				}

				Object value = getTypedValue(element, setting.getValue());

				if (value != null) {
					setSettingValue(setting, value);
				}
			}
		}
	}

	public static void readModules() {
		if (!Files.exists(saveDir.resolve("settings.json"))) {
			BallsLogger.warn("No settings file found, skipping loading.");
			return;
		}

		try (FileReader reader = new FileReader(saveDir.resolve("settings.json").toFile())) {

			JsonObject settings = JsonParser.parseReader(reader).getAsJsonObject();
			String watermark = settings.get("watermark").getAsString();
			BallsHack.title = watermark;
			loadCommands(settings);
			loadFriends(settings);
			JsonObject modules = settings.get("modules").getAsJsonObject();

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
		return null; // Unsupported type
	}

	private static <T> void setSettingValue(ModuleSetting<T> setting, Object value) {
		try {
			setting.setValue((T) value); // Safe cast
		} catch (ClassCastException e) {
			BallsLogger.error("Failed to set value for setting: " + setting.getName());
			e.printStackTrace();
		}
	}

}
