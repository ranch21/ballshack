package org.ranch.ballshack.gui.windows.widgets;

import org.ranch.ballshack.gui.windows.Window;

public class Widget extends Window {

	public Widget(String title, int x, int y, int width, int height) {
		super(title, x, y, width, height);
		hasTitle = false;
	}
}
