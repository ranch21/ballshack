package org.ranch.ballshack.setting;

import com.google.gson.*;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.Constants;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleSettingSaver {

	private static final Path path = BallsHack.getSaveDir();
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static final AtomicBoolean SHOULD_SAVE = new  AtomicBoolean(false);
	private static final ScheduledExecutorService SAVE_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(ModuleSettingSaver::save));
		SAVE_EXECUTOR.scheduleAtFixedRate(
				() -> {
					if (SHOULD_SAVE.getAndSet(false)) {
						save();
					}
				}, 5, 5, TimeUnit.SECONDS
		);
	}

	private static JsonObject getModuleJson(Module module) {
		JsonObject moduleJson = new JsonObject();
		moduleJson.addProperty("enabled", module.isEnabled());

		for (ModuleSettingsGroup group : module.getSettings()) {
			JsonObject groupJson = new JsonObject();
			for (ModuleSetting<?, ?> setting : group.getSettings()) {
				groupJson.add(setting.getName(), setting.getJson());
			}
			moduleJson.add(group.name, groupJson);
		}

		return moduleJson;
	}

	private static void setModule(JsonObject moduleJson, Module module) {
		if (moduleJson.get("enabled").getAsBoolean() && !module.isMeta()) module.onEnable();
		for (ModuleSettingsGroup group : module.getSettings()) {
			JsonObject groupJson = moduleJson.getAsJsonObject(group.name);
			for (ModuleSetting<?, ?> setting : group.getSettings()) {
				setting.readJson(groupJson.get(setting.getName()));
			}
		}
	}

	public static void markDirty() {
		SHOULD_SAVE.set(true);
	}

	public static void load() {
		if (!Files.exists(path.resolve("settings.json"))) {
			BallsLogger.warn("No settings file found, skipping loading.");
			return;
		}

		try (FileReader reader = new FileReader(path.resolve("settings.json").toFile())) {

			JsonObject settings = JsonParser.parseReader(reader).getAsJsonObject();
			JsonElement settings_format_version = settings.get("version");

			if (settings_format_version == null || settings_format_version.getAsInt() != Constants.SETTINGS_FORMAT_VERSION) {
				BallsLogger.warn("Incompatible settings file format, creating backup and skipping loading.");
				Files.copy(path.resolve("settings.json"), path.resolve("settings_backup.json"), StandardCopyOption.REPLACE_EXISTING);
				return;
			}
			JsonObject modules = settings.get("modules").getAsJsonObject();

			for (String modName : modules.keySet()) {
				setModule(modules.get(modName).getAsJsonObject(), ModuleManager.getModuleByName(modName));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		JsonObject json = new JsonObject();
		JsonObject modules = new JsonObject();

		for (Module mod : ModuleManager.getModules()) {
			modules.add(mod.getName(), getModuleJson(mod));
		}

		json.addProperty("version", Constants.SETTINGS_FORMAT_VERSION);
		json.add("modules", modules);

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try (Writer writer = new FileWriter(path.resolve("settings.json").toFile())) {
			gson.toJson(json, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}