package org.ranch.ballshack.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class DebugRenderer {

	private boolean enabled;

	public DebugRenderer(String id) {
		this.enabled = false;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public abstract void render(DrawContext context);
}
