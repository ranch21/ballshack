package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.windows.Window;

import java.util.function.BiConsumer;

public class ButtonWidget extends PressableWidget {

	private BiConsumer<Window, Click> callback;

	public ButtonWidget(String title, int x, int y, int width, int height, BiConsumer<Window, Click> callback) {
		super(title, x, y, width, height);
		this.callback = callback;
	}

	public ButtonWidget(String title, int x, int y, int width, int height) {
		this(title, x, y, width, height, (w, c) -> {
		});
	}

	public void setCallback(BiConsumer<Window, Click> callback) {
		this.callback = callback;
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		text(title, (getWidth() - mc.textRenderer.getWidth(title)) / 2, (getHeight() - mc.textRenderer.fontHeight) / 2, 0xFFFFFFFF, true);
	}

	@Override
	public void onPress(Widget widget, Click click) {
		callback.accept(widget, click);
	}
}
