package org.ranch.ballshack.module;

public enum ModuleAnchor {
	CENTER,
	TOP_CENTER,
	BOTTOM_CENTER,
	TOP_LEFT,
	TOP_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_RIGHT;

	public static final float MARGIN = 0.25f;

	public boolean isRight() {
		return this == TOP_RIGHT || this == BOTTOM_RIGHT;
	}

	public boolean isLeft() {
		return this == TOP_LEFT || this == BOTTOM_LEFT;
	}

	public boolean isCenter() {
		return this == TOP_CENTER || this == BOTTOM_CENTER || this == CENTER;
	}

	public boolean isBottom() {
		return this == BOTTOM_LEFT || this == BOTTOM_RIGHT || this == BOTTOM_CENTER;
	}

	public boolean isTop() {
		return this == TOP_LEFT || this == TOP_RIGHT || this == TOP_CENTER;
	}

	public int getX(int width) {
		return switch (this) {
			case TOP_LEFT, BOTTOM_LEFT -> 0;
			case BOTTOM_CENTER, CENTER, TOP_CENTER -> width / 2;
			case TOP_RIGHT, BOTTOM_RIGHT -> width;
		};
	}

	public int getY(int height) {
		return switch (this) {
			case TOP_LEFT, TOP_RIGHT, TOP_CENTER -> 0;
			case CENTER -> height / 2;
			case BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_CENTER -> height;
		};
	}
}
