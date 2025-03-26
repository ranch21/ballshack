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
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

public class TextFieldWidget extends Widget {

	private boolean selected = false;
	private int counter;
	private String text = "";

	public TextFieldWidget(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public TextFieldWidget(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		this.text = text;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY) {

		if (selected) {
			counter++;
		}

		DrawUtil.drawOutline(context,x,y,width,height);
		context.fill(x,y,x+width,y+height, Colors.CLICKGUI_BACKGROUND.hashCode());
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int yInset = (height - textRend.fontHeight) / 2;
		boolean line = selected && (counter / 100) % 2 == 0;
		DrawUtil.drawText(context, textRend, line ? text + "|" : text, x + 1, y + yInset + 1, Color.WHITE, true);

	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && GuiUtil.mouseOverlap(mouseX,mouseY,x,y,width,height)) {
			selected = true;
			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (selected) {
			if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !text.isEmpty()) {
				text = text.substring(0, text.length() - 1);
			} else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				selected = false;
			}
		}
	}

	@Override
	public void charTyped(char chr, int modifiers) {
		if (selected) {
			text = text + chr;
		}
	}

	public String getText() {
		return text;
	}
}
