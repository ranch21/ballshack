package org.ranch.ballshack.setting;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.function.Supplier;

public abstract class ModuleSetting<T, SELF extends ModuleSetting<T, SELF>> {

	private final String name;
	private String tooltip;
	private Supplier<Boolean> dependencyCondition;

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

	public int renderBase(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
		this.context = context;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if (!dependencyMet())
			return 0;
		int added = render(mouseX, mouseY);
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			DrawUtil.queueTooltip(x + width + 1, y, tooltip);
		}
		return added;
	}

	@SuppressWarnings("unchecked")
	protected SELF self() {
		return (SELF) this;
	}

	public SELF featured() {
		featured = true;
		return self();
	}

	public SELF tooltip(String tooltip) {
		this.tooltip = tooltip;
		return self();
	}

	public SELF depends(Supplier<Boolean> condition) {
		this.dependencyCondition = condition;
		return self();
	}

	public boolean dependencyMet() {
		if (dependencyCondition == null)
			return true;
		return dependencyCondition.get();
	}

	public abstract int render(int mouseX, int mouseY);

	public boolean mouseClickedBase(double mouseX, double mouseY, int button) {
		if (!dependencyMet())
			return false;
		return mouseClicked(mouseX, mouseY, button);
	}

	public void mouseReleasedBase(double mouseX, double mouseY, int button) {
		if (!dependencyMet())
			return;
		mouseReleased(mouseX, mouseY, button);
	}

	public void keyPressedBase(int keyCode, int scanCode, int modifiers) {
		if (!dependencyMet())
			return;
		keyPressed(keyCode, scanCode, modifiers);
	}

	public boolean charTypedBase(char chr, int modifiers) {
		if (!dependencyMet())
			return false;
		return charTyped(chr, modifiers);
	}

	@SuppressWarnings("UnusedReturnValue")
	protected boolean mouseClicked(double mouseX, double mouseY, int button) {
		return false;
	}

	protected void mouseReleased(double mouseX, double mouseY, int button) {}

	protected void keyPressed(int keyCode, int scanCode, int modifiers) {}

	protected boolean charTyped(char chr, int modifiers) {
		return false;
	}

	protected void drawText(DrawContext context, String text) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		context.drawText(textRend, text, x + 2, y + textInset, Color.WHITE.hashCode(), true);
	}

	protected void drawTextRightAligned(DrawContext context, String text) {
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		context.drawText(textRend, text, x + width - textRend.getWidth(text) - 2, y + textInset, Color.WHITE.hashCode(), true);
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

	public abstract String getFormattedValue();

	public abstract JsonObject getJson();

	public abstract void readJson(JsonObject jsonObject);
}
