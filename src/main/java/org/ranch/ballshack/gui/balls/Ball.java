package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Ball {

	private double x;
	private double y;

	private double xVel;
	private double yVel;

	private int size = 12;

	private Color color;

	public Ball(double x, double y, double xVel, double yVel, Color color) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		this.color = color;
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

		int x1 = (int) x;
		int y1 = (int) y;
		int x2 = (int) (x + size);
		int y2 = (int) (y + size);

		context.fill(x1 + 2, y1, x2 - 2, y2, color.darker().hashCode());
		context.fill(x1, y1 + 2, x2, y2 - 2, color.darker().hashCode());

		context.fill(x1 + 2, y1 + 2, x1 + 5, y1 + 5, color.brighter().hashCode());
	}
}
