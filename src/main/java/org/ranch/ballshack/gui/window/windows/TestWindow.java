package org.ranch.ballshack.gui.window.windows;

import org.ranch.ballshack.gui.window.Window;
import org.ranch.ballshack.gui.window.widgets.ButtonWidget;
import org.ranch.ballshack.gui.window.widgets.TextFieldWidget;

public class TestWindow extends Window {
	public TestWindow(int x, int y, int width, int height) {
		super(x, y, width, height, "Test");
		opened = false;
	}

	@Override
	public void setup() {
		widgets.add(new ButtonWidget(5, 5, 100, 10, "alo"));
		widgets.add(new TextFieldWidget(5, 20, 50, 10));
	}
}
