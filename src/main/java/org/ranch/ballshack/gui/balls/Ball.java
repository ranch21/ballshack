package org.ranch.ballshack.gui.balls;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.joml.Vector2d;

public class Ball {

	public Vector2d pos;
	private final Vector2d prevPos;
	private final Vector2d acc;

	public static int size = 8;

	private final Identifier texture;

	public Ball(double x, double y, double xVel, double yVel, Identifier texture) {
		this.pos = new Vector2d(x, y);
		this.prevPos = new Vector2d(pos);
		this.acc = new Vector2d(xVel, yVel);
		this.texture = texture;
	}

	public void update(double deltaT, double gravity) {

		accelerate(new Vector2d(0, gravity));

		Vector2d vel = new Vector2d(pos).sub(prevPos);

		double x = pos.x + vel.x + (acc.x * deltaT * deltaT);
		double y = pos.y + vel.y + (acc.y * deltaT * deltaT);
		acc.set(0, 0);

		prevPos.set(pos.x, pos.y);
		pos.set(x, y);
	}

	public void collideWalls(int width, int height) {
		if (pos.x + size > width) {
			pos.x = width - size;
		} else if (pos.x < 0) {
			pos.x = 0;
		}

		if (pos.y + size > height) {
			pos.y = height - size;
		} else if (pos.y < 0) {
			pos.y = 0;
		}
	}

	public void collideOther(Ball other) {
		double dist = pos.distance(other.pos);
		if (dist < size) {
			Vector2d diff = new Vector2d(pos).sub(other.pos);
			diff.div(dist);
			double delta = size - dist;
			pos.set(pos.x + 0.5 * delta * diff.x, pos.y + 0.5 * delta * diff.y);
			other.pos.set(other.pos.x - 0.5 * delta * diff.x, other.pos.y - 0.5 * delta * diff.y);
		}
	}

	public void collideRect(Rect rect) {
		if (pos.x + size > rect.pos.x &&
				pos.x < rect.farCorner().x &&
				pos.y + size > rect.pos.y &&
				pos.y < rect.farCorner().y
		) {
			double overlapLeft = (pos.x + size) - rect.pos.x;
			double overlapRight = rect.farCorner().x - pos.x;
			double overlapX = Math.min(overlapLeft, overlapRight);

			double overlapTop = (pos.y + size) - rect.pos.y;
			double overlapBottom = rect.farCorner().y - pos.y;
			double overlapY = Math.min(overlapTop, overlapBottom);

			if (overlapX < overlapY) {
				if (overlapLeft < overlapRight) {
					// Collision with left side of the box
					pos.x = rect.pos.x - size;
				} else {
					// Collision with right side of the box
					pos.x = rect.farCorner().x;
				}
			} else {
				if (overlapTop < overlapBottom) {
					// Collision with top of the box
					pos.y = rect.pos.y - size;
				} else {
					// Collision with bottom of the box
					pos.y = rect.farCorner().y;
				}
			}
		}
	}

	public void accelerate(Vector2d acc) {
		this.acc.add(acc);
	}

	public void render(DrawContext context) {
		context.drawTexture(RenderLayer::getGuiTextured, texture, (int) pos.x, (int) pos.y, 0, 0, size, size, size, size);
	}
}
