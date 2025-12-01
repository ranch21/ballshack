package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BallHandler {

	private final List<Ball> balls = new ArrayList<>();

	private HashMap<Integer, List<Ball>> grid = new HashMap<>();

	public double gravity = 10.0;
	public boolean winCollide = true;

	private static final Identifier[] TEXTURES = {
			Identifier.of("ballshack", "ball1.png"),
			Identifier.of("ballshack", "ball2.png"),
			Identifier.of("ballshack", "ball3.png")
	};

	private final Vector2d tempVec = new Vector2d();

	public void spawnBalls(int amount, int width, int height) {
		for (int i = 0; i < amount; i++) {
			Identifier texture = TEXTURES[(int) (Math.random() * TEXTURES.length)];
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

	public int hash(int x, int y) {
		int h = x * 73856093 ^ y * 19349663;
		return h;
	}

	int[][] dirs = {
			{0, 0},   // current cell
			{1, 0},   // right
			{1, 1},   // right-up
			{0, 1},   // up
			{-1, 1},  // left-up
			{-1, 0},  // left
			{-1, -1}, // left-down
			{0, -1},  // down
			{1, -1}   // right-down
	};

	public void update(int width, int height, double deltaT,
					   List<Rect> rects, int mouseX, int mouseY, boolean holding) {

		final int ballCount = balls.size();

		grid.clear();

		for (int i = 0; i < ballCount; i++) {
			Ball ball = balls.get(i);

			int gx = (int)ball.pos.x/Ball.size;
			int gy = (int)ball.pos.y/Ball.size;

			grid.computeIfAbsent(hash(gx, gy), k -> new ArrayList<>()).add(ball);

			if (holding) {
				tempVec.set(mouseX - ball.pos.x, mouseY - ball.pos.y);
				double lenSq = tempVec.lengthSquared();

				if (lenSq > 1e-12) {
					tempVec.normalize().mul(4); // tiny attraction
					ball.accelerate(tempVec);
				}
			}

			ball.update(deltaT, gravity);

			for (int j = 0; j<9;j++) {
				List<Ball> list = grid.get(hash(gx+dirs[j][0], gy+dirs[j][1]));
				if (list != null) {
					for (int k = 0; k < list.size(); k++) {
						ball.collideOther(list.get(k));
					}
				}
			}

			ball.collideWalls(width, height);

			if (winCollide) {
				for (Rect rect : rects) {
					ball.collideRect(rect);
				}
			}
		}
	}

	public void render(DrawContext context) {
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).render(context);
		}
	}
}
