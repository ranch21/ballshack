package org.ranch.ballshack.util.rendering;

import java.awt.*;
import java.awt.color.ColorSpace;

public class BallColor extends Color {

	public BallColor(int r, int g, int b) {
		super(r, g, b);
	}

	public BallColor(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public BallColor(int rgb) {
		super(rgb);
	}

	public BallColor(int rgba, boolean hasalpha) {
		super(rgba, hasalpha);
	}

	public BallColor(float r, float g, float b) {
		super(r, g, b);
	}

	public BallColor(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public BallColor(ColorSpace cspace, float[] components, float alpha) {
		super(cspace, components, alpha);
	}

	public static BallColor fromColor(Color c) {
		return new BallColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public BallColor setRed(float r) {
		return setRed((int) (r * 255));
	}

	public BallColor setRed(int r) {
		return new BallColor(r, this.getGreen(), this.getBlue(), this.getAlpha());
	}

	public BallColor setGreen(float g) {
		return setGreen((int) (g * 255));
	}

	public BallColor setGreen(int g) {
		return new BallColor(this.getRed(), g, this.getBlue(), this.getAlpha());
	}

	public BallColor setBlue(float b) {
		return setBlue((int) (b * 255));
	}

	public BallColor setBlue(int b) {
		return new BallColor(this.getRed(), this.getGreen(), b, this.getAlpha());
	}

	public BallColor setAlpha(float a) {
		return setAlpha((int) (a * 255));
	}

	public BallColor setAlpha(int a) {
		return new BallColor(this.getRed(), this.getGreen(), this.getBlue(), a);
	}
}
