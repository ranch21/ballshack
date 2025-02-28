package org.ranch.ballshack.module;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.setting.ModuleSettings;

public class Module {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	private ModuleSettings settings;
	private String name;
	private ModuleCategory category;
	private Boolean subscribed;
	private int bind;

	public boolean enabled;

	public Module(String name, ModuleCategory category, int bind) {
		this.name = name;
		this.category = category;
		this.bind = bind;
		//this.settings = settings;
	}

	public void onEnable() {
		BallsLogger.info("Enabled module " + name);
		subscribed = BallsHack.eventBus.subscribe(this);
		enabled = true;
	}

	public void onDisable() {
		BallsLogger.info("Disabled module " + name);
		if (subscribed) {
			subscribed = !BallsHack.eventBus.unsubscribe(this);
		}
		enabled = false;
	}

	public int getBind() {
		return bind;
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
}
