package org.ranch.ballshack.gui.windows.widgets;

import org.ranch.ballshack.gui.windows.IWindow;
import org.ranch.ballshack.gui.windows.Window;

import java.util.Comparator;

public interface ListWidget extends IWindow {
	default void arrangeChildren() {
		int lastY = 1;
		getChildren().sort(Comparator.comparing(a -> a.title));
		for (Window window : getChildren()) {
			window.setY(lastY);
			window.setX(1);
			window.setWidth(getWidth() - 2);
			lastY += window.getHeight() + 2;
		}
	}
}
