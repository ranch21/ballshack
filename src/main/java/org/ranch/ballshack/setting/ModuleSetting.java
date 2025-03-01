package org.ranch.ballshack.setting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public abstract class ModuleSetting<T> {

	private String name;

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	private T value;

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

	protected void drawValue(DrawContext context) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		context.drawText(textRend, Text.literal(this.getName() + ": " + this.getValue()),x + 2,y + textInset,0xFFFFFFFF,true);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		SaveHelper.SCHEDULE_SAVE_MODULES.set(true);
	}

	public String getName() {
		return name;
	}
}
