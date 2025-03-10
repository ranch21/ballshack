package org.ranch.ballshack.gui.window;

import net.minecraft.client.gui.DrawContext;

public abstract class Widget {

	int x;
	int y;
	int width;
	int height;

	public Widget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY);

	public abstract void mouseClicked(double mouseX, double mouseY, int button);

	public abstract void mouseReleased(double mouseX, double mouseY, int button);

	public abstract void keyPressed(int keyCode, int scanCode, int modifiers);
}
