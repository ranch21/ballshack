package org.ranch.ballshack.gui.window.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.window.Widget;

import java.awt.*;

public class TextWidget extends Widget {

	public String text;

	public TextWidget(int x, int y, String text) {
		super(x, y, 1, 1);
		this.text = text;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRend, text, x, y, Color.WHITE.hashCode(), true);
		width = textRend.getWidth(text);
		height = textRend.fontHeight;
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {

	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {

	}

	@Override
	public void charTyped(char chr, int modifiers) {

	}
}
