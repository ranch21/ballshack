package org.ranch.ballshack.gui.window.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.window.Widget;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;

public class ButtonWidget extends Widget {

	private boolean pressed;
	private final String text;

	public ButtonWidget(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		this.text = text;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY) {

		DrawUtil.drawOutline(context, x, y, width, height);
		Color c = GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) ? Colors.SELECTABLE.brighter() : Colors.SELECTABLE;
		context.fill(x, y, x + width, y + height, c.hashCode());
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int yInset = (height - textRend.fontHeight) / 2;
		int xInset = (width - textRend.getWidth(text)) / 2;
		context.drawText(textRend, text, x + xInset, y + yInset, Color.WHITE.hashCode(), true);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			pressed = true;
			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
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

	public boolean wasPressed() {
		if (pressed) {
			pressed = false;
			return true;
		}
		return false;
	}
}
