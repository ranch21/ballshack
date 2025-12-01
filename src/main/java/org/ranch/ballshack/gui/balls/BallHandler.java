package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Vector2d;
import org.ranch.ballshack.BallsLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.ranch.ballshack.gui.balls.Ball.size;

public class BallHandler {

	private final List<Ball> balls = new ArrayList<>();

	public HashMap<Integer, List<Ball>> grid = new HashMap<>();

	public double gravity = 10.0;
	public boolean winCollide = true;

	public static final int COLLISION_ITERATIONS = 4;

	public long frameTime = 0;

	private static final Identifier[] TEXTURES = {
			Identifier.of("ballshack", "ball1.png"),
			Identifier.of("ballshack", "ball2.png"),
			Identifier.of("ballshack", "ball3.png")
	};

	public void spawnBalls(int amount, int width, int height, List<Rect> rects) {
		int spawned = 0;
		for (int i = 0; i < amount; i++) {
			Identifier texture = TEXTURES[(int) (Math.random() * TEXTURES.length)];

			for (int j = 0; j < 10; j++) {
				double x = Math.random() * (width - size);
				double y = Math.random() * (height - size);
				boolean colliding = false;

				for (Ball ball : balls) {
					double dx = x - ball.pos.x;
					double dy = y - ball.pos.y;

					double distSq = dx * dx + dy * dy;
					double minDist = size;

					if (distSq < minDist * minDist) {
						colliding = true;
						break;
					}
				}
				if (!colliding && winCollide) {
					for (Rect rect : rects) {
						if (x + size > rect.pos.x &&
								x < rect.farCorner().x &&
								y + size > rect.pos.y &&
								y < rect.farCorner().y) {
							colliding = true;
							break;
						}
					}
				}
				if (!colliding || j == 9) {
					balls.add(new Ball(x, y, 0, 0, texture));
					spawned++;
					break;
				}
			}
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

	public int getGridSize() {
		return size * 4;
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

	public void update(int width, int height, double deltaT, List<Rect> rects) {

		final int ballCount = balls.size();

		grid.clear();

		for (int i = 0; i < ballCount; i++) {
			Ball ball = balls.get(i);

			int gx = (int)ball.pos.x/ getGridSize();
			int gy = (int)ball.pos.y/ getGridSize();

			grid.computeIfAbsent(hash(gx, gy), k -> new ArrayList<>()).add(ball);

			/*if (holding) {
				tempVec.set(mouseX - ball.pos.x, mouseY - ball.pos.y);
				double lenSq = tempVec.lengthSquared();

				if (lenSq > 1e-12) {
					tempVec.normalize().mul(4); // tiny attraction
					ball.accelerate(tempVec);
				}
			}*/

			ball.update(deltaT, gravity * 250);

			for (int l = 0; l < COLLISION_ITERATIONS; l++) {
				for (int j = 0; j<9;j++) {
					List<Ball> list = grid.get(hash(gx+dirs[j][0], gy+dirs[j][1]));
					if (list != null) {
						for (int k = 0; k < list.size(); k++) {
							ball.collideOther(list.get(k));
						}
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
