package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BallHandler {

	private List<Ball> balls = new ArrayList<Ball>();

	public double gravity = 10;
	public double bounce = 1;

	private Color[] colors = new Color[]{
			Color.BLUE,
			Color.CYAN,
			Color.GREEN,
			Color.MAGENTA,
			Color.ORANGE,
			Color.YELLOW,
	};

	public void spawnBalls(int amount, int width, int height) {
		for (int i = 0; i < amount; i++) {
			Color color = colors[(int) (Math.random() * colors.length)];
			double x = Math.random() * width;
			double y = Math.random() * height;
			double xVel = Math.random() * 10 - 5;
			double yVel = Math.random() * 6 - 3;
			balls.add(new Ball(x, y, xVel, yVel, color));
		}
	}

	public void clearBalls() {
		balls.clear();
	}

	public int getBallCount() {
		return balls.size();
	}

	public void update(int width, int height, double deltaT) {
		for (Ball ball : balls) {
			ball.update(width, height, deltaT, gravity, bounce);
		}
	}

	public void render(DrawContext context) {
		for (Ball ball : balls) {
			ball.render(context);
		}
	}
}
