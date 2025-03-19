package org.ranch.ballshack.gui.window;

import net.minecraft.client.gui.DrawContext;

public abstract class Widget {

	protected int x;
	protected int y;
	private int offX;
	private int offY;
	public int width;
	public int height;

	public Widget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.offX = x;
		this.offY = y;
		this.width = width;
		this.height = height;
	}

	public void render(DrawContext context, int mouseX, int mouseY, int winX, int winY) {
		x = winX + offX;
		y = winY + offY;
		render(context, mouseX, mouseY);
	};

	public abstract void render(DrawContext context, int mouseX, int mouseY);

	public abstract void mouseClicked(double mouseX, double mouseY, int button);

	public abstract void mouseReleased(double mouseX, double mouseY, int button);

	public abstract void keyPressed(int keyCode, int scanCode, int modifiers);

	public void setX(int x) {
		this.offX = x;
	}

	public void setY(int y) {
		this.offY = y;
	}

	public int getX() {
		return offX;
	}

	public int getY() {
		return offY;
	}
}
