package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class Ball {

	private double x;
	private double y;

	private double xVel;
	private double yVel;

	private int size = 16;

	private Identifier texture;

	public Ball(double x, double y, double xVel, double yVel, Identifier texture) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		this.texture = texture;
	}

	public void update(int width, int height, double deltaT, double gravity, double bounce) {

		yVel += gravity * deltaT; //cm per sec

		x += xVel * deltaT;
		y += yVel * deltaT;

		if (x + size > width) {
			xVel = -xVel;
			x = width - size;
		} else if (x < 0) {
			xVel = -xVel;
			x = 0;
		}

		if (y + size > height) {
			yVel = -(Math.random() * 40 + 20) * bounce;
			y = height - size;
		} else if (y < 0) {
			yVel = -yVel;
			y = 0;
		}
	}

	public void render(DrawContext context) {
		context.drawTexture(RenderLayer::getGuiTextured, texture, (int) x, (int) y, 0, 0, 16, 16, 16, 16);
	}
}
