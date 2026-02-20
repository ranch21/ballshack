package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.DrawContext;

public class TextWidget extends Widget {
	public TextWidget(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height);
		addFlags(NO_FILL | NO_TITLE | NO_BORDER);
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		setWidth(mc.textRenderer.getWidth(title));
		setHeight(mc.textRenderer.fontHeight);

		text(title, 0, 0, 0xFFFFFFFF, true);
	}
}
