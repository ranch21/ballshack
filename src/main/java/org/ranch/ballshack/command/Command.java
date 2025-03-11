package org.ranch.ballshack.command;

import net.minecraft.client.MinecraftClient;

public abstract class Command {

	protected final MinecraftClient mc = MinecraftClient.getInstance();

	protected String name;

	public Command(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract void onCall(String[] args);
}
