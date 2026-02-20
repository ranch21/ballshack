package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.windows.Window;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ButtonWidget extends Widget {

	private final BiConsumer<Window, Click> callback;

	public ButtonWidget(String title, int x, int y, int width, int height, BiConsumer<Window, Click> callback) {
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
			callback.accept(this, click);
			return true;
		}
		return false;
	}
}
