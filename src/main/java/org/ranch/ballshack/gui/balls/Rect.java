package org.ranch.ballshack.gui.balls;

import org.joml.Vector2d;

public class Rect {
	public final Vector2d pos;
	public final Vector2d size;

	public Rect(Vector2d pos, Vector2d size) {
		this.pos = pos;
		this.size = size;
	}

	public Vector2d farCorner() {
		return new Vector2d(pos.x + size.x, pos.y + size.y);
	}
}
