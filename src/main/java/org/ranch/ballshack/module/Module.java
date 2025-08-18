package org.ranch.ballshack.module;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.SettingSaver;

import java.util.ArrayList;

public abstract class Module {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	protected ModuleSettings settings;
	private final String name;
	private final ModuleCategory category;
	private Boolean subscribed;
	private final boolean isMeta;

	private final String tooltip;

	protected boolean enabled;

	public Module(String name, ModuleCategory category, int bind) {
		this(name, category, bind, new ModuleSettings(new ArrayList<>()), null, false);
	}

	public Module(String name, ModuleCategory category, int bind, String tooltip) {
		this(name, category, bind, new ModuleSettings(new ArrayList<>()), tooltip, false);
	}

	public Module(String name, ModuleCategory category, int bind, String tooltip, boolean isMeta) {
		this(name, category, bind, new ModuleSettings(new ArrayList<>()), tooltip, isMeta);
	}

	public Module(String name, ModuleCategory category, int bind, ModuleSettings settings) {
		this(name, category, bind, settings, null, false);
	}

	public Module(String name, ModuleCategory category, int bind, ModuleSettings settings, @Nullable String tooltip) {
		this(name, category, bind, settings, tooltip, false);
	}

	public Module(String name, ModuleCategory category, int bind, ModuleSettings settings, @Nullable String tooltip, boolean isMeta) {
		this.name = name;
		this.category = category;
		settings.getBind().setValue(bind);
		this.settings = settings;
		this.tooltip = tooltip;
		this.isMeta = isMeta;
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

	public String getTooltip() {
		return tooltip;
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

	public boolean isMeta() {
		return isMeta;
	}


	public boolean isSubscribed() {
		return subscribed;
	}
}
