package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.ranch.ballshack.gui.window.CategoryWindow;

import java.util.ArrayList;
import java.util.List;

public class BallHandler {

	private List<Ball> balls = new ArrayList<Ball>();

	public double gravity = 10;
	public boolean winCollide = true;

	private Identifier[] textures = new Identifier[]{
			Identifier.of("ballshack", "ball1.png"),
			Identifier.of("ballshack", "ball2.png"),
			Identifier.of("ballshack", "ball3.png")
	};

	public void spawnBalls(int amount, int width, int height) {
		for (int i = 0; i < amount; i++) {
			Identifier texture = textures[(int) (Math.random() * textures.length)];
			double x = Math.random() * width;
			double y = Math.random() * height;
			double xVel = Math.random() * 10 - 5;
			double yVel = Math.random() * 6 - 3;
			balls.add(new Ball(x, y, xVel, yVel, texture));
		}
	}

	public void clearBalls() {
		balls.clear();
	}

	public int getBallCount() {
		return balls.size();
	}

	public void update(int width, int height, double deltaT, List<CategoryWindow> wins) {
		for (Ball ball : balls) {
			ball.update(deltaT, gravity);
			for (Ball other : balls) {
				if (other == ball) continue;

				ball.collideOther(other);
			}
			for (CategoryWindow window : wins) {
				Box box = new Box(window.x, window.y, 0, window.x + CategoryWindow.width, window.y + window.getHeight(), 0);
				ball.collideRect(box);
			}
			ball.collideWalls(width, height);
		}
	}

	public void render(DrawContext context) {
		for (Ball ball : balls) {
			ball.render(context);
		}
	}
}
