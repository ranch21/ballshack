package org.ranch.ballshack.setting;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

public abstract class ModuleSetting<T> {

	private final String name;
	private String tooltip;

	private boolean featured;

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	protected DrawContext context;

	protected T value;

	public ModuleSetting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
		this.context = context;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		int added = render(mouseX, mouseY);
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			DrawUtil.queueTooltip(x + width + 1, y, tooltip);
		}
		return added;
	}

	public abstract int render(int mouseX, int mouseY);

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {

	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {

	}

	public boolean charTyped(char chr, int modifiers) {
		return false;
	}

	protected void drawText(DrawContext context, String text) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		DrawUtil.drawText(context, textRend, text, x + 2, y + textInset, Color.WHITE, true);
	}

	protected void drawTextRightAligned(DrawContext context, String text) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		DrawUtil.drawText(context, textRend, text, x + width - textRend.getWidth(text) - 2, y + textInset, Color.WHITE, true);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		SettingSaver.SCHEDULE_SAVE.set(true);
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

	public ModuleSetting<T> tooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public abstract String getFormattedValue();

	public abstract JsonObject getJson();

	public abstract void readJson(JsonObject jsonObject);
}
