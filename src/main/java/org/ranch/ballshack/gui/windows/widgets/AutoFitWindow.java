package org.ranch.ballshack.gui.windows.widgets;

import org.ranch.ballshack.gui.windows.Window;

public class AutoFitWindow extends Window {

	protected boolean xFit;
	protected boolean yFit;
	protected int xPad;
	protected int yPad;

	public AutoFitWindow(String title, int x, int y, int width, int height, boolean xFit, boolean yFit, int xPad, int yPad) {
		super(title, x, y, width, height);
		this.xFit = xFit;
		this.yFit = yFit;
		this.xPad = xPad;
		this.yPad = yPad;
	}

	@Override
	public void addChild(Window child) {
		super.addChild(child);
		fit();
	}

	protected void fit() {
		if (xFit) fitWidth();
		if (yFit) fitHeight();
	}

	protected void fitHeight() {
		setHeight(getMaxScrollY() + yPad);
	}

	protected void fitWidth() {
		setWidth(getMaxScrollX() + xPad);
	}
}
