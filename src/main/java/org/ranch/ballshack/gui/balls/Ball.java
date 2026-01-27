package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2d;

public class Ball {

	public final Vector2d pos;
	private final Vector2d prevPos;
	private final Vector2d acc;

	public static int size = 8;
	private final Identifier texture;

	public static final double MAX_SPEED = 3;

	public Ball(double x, double y, double xVel, double yVel, Identifier texture) {
		this.pos = new Vector2d(x, y);
		this.prevPos = new Vector2d(x - xVel, y - yVel);
		this.acc = new Vector2d(xVel, yVel);
		this.texture = texture;
	}

	public void update(double deltaT, double gravity) {
		accelerate(new Vector2d(0, gravity));

		double tempX = pos.x;
		double tempY = pos.y;

		pos.x = pos.x + MathHelper.clamp((pos.x - prevPos.x) + acc.x * deltaT * deltaT, -MAX_SPEED, MAX_SPEED);
		pos.y = pos.y + MathHelper.clamp((pos.y - prevPos.y) + acc.y * deltaT * deltaT, -MAX_SPEED, MAX_SPEED);

		prevPos.x = tempX;
		prevPos.y = tempY;

		acc.set(0, 0);
	}

	public void collideWalls(int width, int height) {
		double maxX = width - size;
		double maxY = height - size;

		if (pos.x > maxX) pos.x = maxX;
		else if (pos.x < 0) pos.x = 0;

		if (pos.y > maxY) pos.y = maxY;
		else if (pos.y < 0) pos.y = 0;
	}

	public void collideOther(Ball other) {

		double dx = pos.x - other.pos.x;
		double dy = pos.y - other.pos.y;

		double distSq = dx * dx + dy * dy;
		double minDist = size;

		if (distSq < minDist * minDist) {
			double dist = Math.sqrt(distSq);
			if (dist == 0) return;

			double overlap = (minDist - dist) * 0.5;
			overlap = Math.min(overlap, size * 0.5);
			double invDist = 1.0 / dist;

			double ox = dx * invDist * overlap;
			double oy = dy * invDist * overlap;

			pos.x += ox;
			pos.y += oy;
			other.pos.x -= ox;
			other.pos.y -= oy;
		}
	}

	public void collideRect(Rect rect) {
		double left = rect.pos.x;
		double top = rect.pos.y;
		double right = rect.farCorner().x;
		double bottom = rect.farCorner().y;

		if (pos.x + size > left &&
				pos.x < right &&
				pos.y + size > top &&
				pos.y < bottom) {

			double overlapLeft = (pos.x + size) - left;
			double overlapRight = right - pos.x;
			double overlapTop = (pos.y + size) - top;
			double overlapBottom = bottom - pos.y;

			if (Math.min(overlapLeft, overlapRight) < Math.min(overlapTop, overlapBottom)) {
				pos.x = (overlapLeft < overlapRight) ? left - size : right;
			} else {
				pos.y = (overlapTop < overlapBottom) ? top - size : bottom;
			}
		}
	}

	public void accelerate(Vector2d a) {
		acc.add(a);
	}

	public void render(DrawContext context) {
		context.drawTexture(
				RenderPipelines.GUI_TEXTURED,
				texture,
				(int) pos.x,
				(int) pos.y,
				0, 0,
				size, size,
				size, size
		);
	}
}
