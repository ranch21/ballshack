package org.ranch.ballshack.setting.client;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.setting.module.ModuleSettingSaver;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


//todo maybe change to another toml lib (JToml)
public class ClientSettingSaver {
	private static final Map<String, ClientSetting<?>> settings = new HashMap<>();
	private static final Path file = BallsHack.getSaveDir().resolve("config.toml");
	public static final CommentedFileConfig config = CommentedFileConfig.builder(file)
			.autosave()
			.sync()
			.build();

	public static final AtomicBoolean SHOULD_SAVE = new AtomicBoolean(false);
	private static final ScheduledExecutorService SAVE_EXECUTOR = Executors.newSingleThreadScheduledExecutor();


	static {
		config.load();
		Runtime.getRuntime().addShutdownHook(new Thread(ModuleSettingSaver::save));
		SAVE_EXECUTOR.scheduleAtFixedRate(
				() -> {
					if (SHOULD_SAVE.getAndSet(false)) {
						save();
					}
				}, 5, 5, TimeUnit.SECONDS
		);
	}

	public static void registerSetting(ClientSetting<?> setting) {
		settings.put(setting.getKey(), setting);
	}

	public static Map<String, ClientSetting<?>> getSettings() {
		return settings;
	}

	public static void load() {
		for (Map.Entry<String, ClientSetting<?>> entry : settings.entrySet()) {
			ClientSetting<?> setting = entry.getValue();
			String key = entry.getKey();

			if (!config.contains(key)) continue;

			Object value = config.get(key);

			if (setting.getValue() instanceof Character && value instanceof String s) {
				if (!s.isEmpty()) {
					value = s.charAt(0);
				}
			}

			setting.setRawValue(value);
		}
	}

	public static void save() {
		for (Map.Entry<String, ClientSetting<?>> entry : settings.entrySet()) {
			ClientSetting<?> setting = entry.getValue();
			Object value = setting.getValue();

			if (value instanceof Character c) {
				config.set(entry.getKey(), String.valueOf(c));
			} else {
				config.set(entry.getKey(), value);
			}

			config.setComment(entry.getKey(), entry.getValue().getTooltip());
		}

		config.save();
	}

	public static void markDirty() {
		SHOULD_SAVE.set(true);
	}
}
