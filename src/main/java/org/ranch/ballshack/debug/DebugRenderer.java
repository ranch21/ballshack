package org.ranch.ballshack.debug;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.util.rendering.Renderer;

public abstract class DebugRenderer {

	private boolean enabled;

	public DebugRenderer() {
		this.enabled = false;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public abstract void renderGui(DrawContext context);

	public abstract void render3d(Renderer context, MatrixStack matrixStack);
}
