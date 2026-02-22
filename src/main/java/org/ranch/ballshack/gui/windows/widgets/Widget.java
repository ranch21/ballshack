package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.util.rendering.DrawUtil;

public class Widget extends Window {

	public Widget(String title, int x, int y, int width, int height) {
		super(title, x, y, width, height);
		addFlags(NO_TITLE | NO_SCROLL);
	}
}
