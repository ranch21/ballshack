package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.windows.Window;

import java.util.function.Consumer;

public class ButtonWidget extends Widget {

	private final Consumer<Window> callback;

	public ButtonWidget(String title, int x, int y, int width, int height, Consumer<Window> callback) {
		super(title, x, y, width, height);
		this.callback = callback;
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		text(title, 2, 2, 0xFFFFFFFF, true);
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		super.mouseClicked(click, doubled);
		if (overlaps(click)) {
			callback.accept(this);
			return true;
		}
		return false;
	}
}
