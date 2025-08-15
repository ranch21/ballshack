package org.ranch.ballshack.module;

import java.awt.*;

public enum ModuleCategory {
	PLAYER(Color.BLUE),
	RENDER(Color.YELLOW),
	COMBAT(Color.RED),
	MOVEMENT(Color.MAGENTA),
	WORLD(Color.ORANGE),
	FUN(Color.GREEN),
	HUD(Color.PINK),
	CLIENT(Color.GRAY),;

	private final Color color;

	ModuleCategory(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
