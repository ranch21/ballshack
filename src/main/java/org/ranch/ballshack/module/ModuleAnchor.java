package org.ranch.ballshack.module;

public enum ModuleAnchor {
	CENTER(0, 0),
	TOP(0, -1),
	BOTTOM(0, 1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	TOP_LEFT(-1, -1),
	TOP_RIGHT(1, -1),
	BOTTOM_LEFT(-1, 1),
	BOTTOM_RIGHT(1, 1);

	public final int h;
	public final int v;

	public static final float MARGIN = 0.25f;

    ModuleAnchor(int h, int v) {
        this.h = h;
        this.v = v;
    }

    public boolean isRight() {
		return this.h > 0;
	}

	public boolean isLeft() {
		return this.h < 0;
	}

	public boolean isCenter() {
		return this.h == 0 && this.v == 0;
	}

	public boolean isBottom() {
		return this.v < 0;
	}

	public boolean isTop() {
		return this.v > 0;
	}

	public int getX(int width) {
		float f = (float) (this.h + 1) / 2;
		return (int) (f * width);
	}

	public int getY(int height) {
		float f = (float) (this.v + 1) / 2;
		return (int) (f * height);
	}

	public static ModuleAnchor fromHV(int h, int v) {
		for (ModuleAnchor anchor : ModuleAnchor.values()) {
			if (anchor.h == h && anchor.v == v) {
				return anchor;
			}
		}
		return null;
	}
}
