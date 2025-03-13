package org.ranch.ballshack.module;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.setting.SettingSaver;
import org.ranch.ballshack.setting.ModuleSettings;

import java.util.ArrayList;

public class Module {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	protected final ModuleSettings settings;
	private final String name;
	private final ModuleCategory category;
	private Boolean subscribed;

	protected boolean enabled;

	public Module(String name, ModuleCategory category, int bind) {
		this(name, category, bind, new ModuleSettings(new ArrayList<>()));
	}

	public Module(String name, ModuleCategory category, int bind, ModuleSettings settings) {
		this.name = name;
		this.category = category;
		settings.getBind().setValue(bind);
		this.settings = settings;
	}

	public void onEnable() {
		if (ModuleManager.printToggle) BallsLogger.info("Enabled module " + name);
		subscribed = BallsHack.eventBus.subscribe(this);
		enabled = true;
		SettingSaver.SCHEDULE_SAVE.set(true);
	}

	public void onDisable() {
		if (ModuleManager.printToggle) BallsLogger.info("Enabled module " + name);
		if (subscribed) {
			subscribed = !BallsHack.eventBus.unsubscribe(this);
		}
		enabled = false;
		SettingSaver.SCHEDULE_SAVE.set(true);
	}

	public int getBind() {
		return settings.getBind().getValue();
	}

	public ModuleCategory getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public ModuleSettings getSettings() {
		return settings;
	}

	public void toggle() {
		if (enabled) {
			onDisable();
		} else {
			onEnable();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isSubscribed() {
		return subscribed;
	}
}
