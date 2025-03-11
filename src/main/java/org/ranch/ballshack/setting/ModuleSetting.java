package org.ranch.ballshack.setting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

public abstract class ModuleSetting<T> {

	private String name;

	private boolean featured;

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	protected T value;

	public ModuleSetting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public abstract int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY);

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {

	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {

	}

	protected void drawText(DrawContext context, String text) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		DrawUtil.drawText(context, textRend, text,x + 2,y + textInset, Color.WHITE,true);
	}

	protected void drawTextRightAligned(DrawContext context, String text) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		DrawUtil.drawText(context, textRend, text, x + width - textRend.getWidth(text) - 2,y + textInset, Color.WHITE,true);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		SettingSaver.SCHEDULE_SAVE_MODULES.set(true);
	}

	public String getName() {
		return name;
	}

	public boolean isFeatured() {
		return featured;
	}

	public ModuleSetting<T> featured() {
		featured = true;
		return this;
	}

	public abstract String getFormattedValue();
}
