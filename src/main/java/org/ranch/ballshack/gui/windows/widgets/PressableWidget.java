package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.Cursor;
import net.minecraft.client.gui.cursor.StandardCursors;
import org.ranch.ballshack.gui.Colors;

public abstract class PressableWidget extends Widget {

	public PressableWidget(String title, int x, int y, int width, int height) {
		super(title, x, y, width, height);
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		if (overlaps(mouseX, mouseY)) {
			context.setCursor(StandardCursors.POINTING_HAND);
		}
	}

	@Override
	protected void drawBackground(DrawContext context, double mouseX, double mouseY) {
		if (overlaps(mouseX, mouseY)) {
			fill(0, 0, getWidth(), getHeight(), Colors.SELECTABLE.getColor().darker().hashCode());
		} else {
			fill(0, 0, getWidth(), getHeight(), Colors.CLICKGUI_2.getColor().hashCode());
		}
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		super.mouseClicked(click, doubled);
		if (overlaps(click)) {
			onPress(this, click);
			return true;
		}
		return false;
	}

	public abstract void onPress(Widget widget, Click click);
}
