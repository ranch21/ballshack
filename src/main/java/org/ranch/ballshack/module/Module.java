package org.ranch.ballshack.module;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.SaveHelper;

import java.util.ArrayList;

public class Module {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	private ModuleSettings settings;
	private String name;
	private ModuleCategory category;
	private Boolean subscribed;

	private boolean enabled;

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
		BallsLogger.info("Enabled module " + name);
		subscribed = BallsHack.eventBus.subscribe(this);
		enabled = true;
		SaveHelper.SCHEDULE_SAVE_MODULES.set(true);
	}

	public void onDisable() {
		BallsLogger.info("Disabled module " + name);
		if (subscribed) {
			subscribed = !BallsHack.eventBus.unsubscribe(this);
		}
		enabled = false;
		SaveHelper.SCHEDULE_SAVE_MODULES.set(true);
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
