package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextWidget extends Widget {

	private Text text;

	public TextWidget(String text, int x, int y, int width, int height) {
		this(Text.of(text), x, y, width, height);
	}

	public TextWidget(Text text, int x, int y, int width, int height) {
		super(text.getString(), x, y, width, height);
		addFlags(NO_FILL | NO_TITLE | NO_BORDER);
		this.text = text;
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		setWidth(mc.textRenderer.getWidth(title));
		setHeight(mc.textRenderer.fontHeight);

		text(text, 0, 0, -1, true);
	}
}
