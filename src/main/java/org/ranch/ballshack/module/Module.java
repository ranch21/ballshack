package org.ranch.ballshack.module;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.BallsHack;

public class Module {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	private String name;
	private ModuleCategory category;
	private Boolean subscribed;
	private int bind;

	public boolean enabled;

	public Module(String name, ModuleCategory category, int bind) {
		this.name = name;
		this.category = category;
		this.bind = bind;
	}

	public void onEnable() {
		subscribed = BallsHack.eventBus.subscribe(this);
		enabled = true;
	}

	public void onDisable() {
		if (subscribed) {
			subscribed = !BallsHack.eventBus.unsubscribe(this);
		}
		enabled = false;
	}

	public int getBind() {
		return bind;
	}

	public String getName() {
		return name;
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
