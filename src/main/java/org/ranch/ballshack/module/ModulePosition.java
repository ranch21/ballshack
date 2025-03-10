package org.ranch.ballshack.module;

public enum ModulePosition {
	TOP_LEFT,
	TOP_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_RIGHT;

	public boolean isRight() {
		return this.ordinal() % 2 == 0;
	}

	public boolean isBottom() {
		return this.ordinal() > 1;
	}

	public int getX(int width) {
		if (this.isRight()) {
			return width;
		} else {
			return 0;
		}
	}

	public int getY(int height) {
		if (this.isBottom()) {
			return height;
		} else {
			return 0;
		}
	}
}
