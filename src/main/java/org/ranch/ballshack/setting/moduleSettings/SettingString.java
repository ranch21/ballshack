package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.util.TextUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.List;

public class SettingString extends ModuleSetting<String, SettingString> {

	private boolean focused = false;
	private int selectionStart;
	private int selectionEnd;
	private int maxLength = 256;

	public SettingString(String name, String startingValue) {
		super(name, startingValue);
	}

	public SettingString maxLen(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	@Override
	public int render(int mouseX, int mouseY) {
		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_2.getColor().hashCode());

		drawText(context, this.getName() + ": ");

		String value = getText();
		//boolean caret = caretBlink / 60 % 2 == 0 && focused;
		if (focused) {
			value = new StringBuilder(value).insert(selectionStart, "_").toString();
		}
		List<String> splitLines = TextUtil.wrapText(value, width - 4);

		int textAreaY = y + height;
		int lineHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 1;
		int textAreaHeight = Math.max(lineHeight, splitLines.size() * lineHeight) + 2;
		context.fill(x, textAreaY, x + width, textAreaY + textAreaHeight, Colors.CLICKGUI_2.getColor().hashCode());
		DrawUtil.drawOutline(context, x, textAreaY, width, textAreaHeight - 1);

		TextRenderer tr = MinecraftClient.getInstance().textRenderer;
		int ty = textAreaY + 1;
		for (String line : splitLines) {
			context.drawText(tr, line, x + 2, ty, Color.WHITE.hashCode(), true);
			ty += lineHeight;
		}

		return height + textAreaHeight;
	}

	private void setSelection() {
		selectionStart = getText().length();
		selectionEnd = selectionStart;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height + renderHeightPreview()) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			focused = true;
			setSelection();
			return true;
		} else {
			focused = false;
			return false;
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (!focused) return;

		boolean shift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			focused = false;
			return;
		}
		if (keyCode == GLFW.GLFW_KEY_ENTER) {
			if (shift) {
				write("\n");
			} else {
				focused = false;
			}
			return;
		}
		if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
			erase();
		}
		if (keyCode == GLFW.GLFW_KEY_LEFT) {
			moveCursor(-1);
		}
		if (keyCode == GLFW.GLFW_KEY_RIGHT) {
			moveCursor(1);
		}
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (!focused) {
			return false;
		}
		if (StringHelper.isValidChar(chr)) {
			write(Character.toString(chr));
			return true;
		}

		return false;
	}

	public String getText() {
		return value = getValue() == null ? "" : getValue();
	}

	public void write(String text) {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		int k = this.maxLength - getText().length() - (i - j);
		if (k > 0) {
			String string = StringHelper.stripInvalidChars(text);
			int l = string.length();
			if (k < l) {
				if (Character.isHighSurrogate(string.charAt(k - 1))) {
					k--;
				}

				string = string.substring(0, k);
				l = k;
			}

			String string2 = new StringBuilder(getText()).replace(i, j, string).toString();
			setValue(string2);
			setSelectionStart(i + l);
			setSelectionEnd(selectionStart);
		}
	}

	private void erase() {
		int pos = Util.moveCursor(getText(), this.selectionStart, -1);

		if (!getText().isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				int i = Math.min(pos, this.selectionStart);
				int j = Math.max(pos, this.selectionStart);
				if (i != j) {
					String string = new StringBuilder(getText()).delete(i, j).toString();
					setValue(string);
					setSelectionStart(i);
					setSelectionEnd(i);
				}
			}
		}
	}

	public void moveCursor(int offset) {
		setSelectionStart(getCursorPosWithOffset(offset));
		setSelectionEnd((selectionStart));
	}

	private int getCursorPosWithOffset(int offset) {
		return Util.moveCursor(getText(), this.selectionStart, offset);
	}

	public void setSelectionStart(int cursor) {
		this.selectionStart = MathHelper.clamp(cursor, 0, getText().length());
	}

	public void setSelectionEnd(int index) {
		this.selectionEnd = MathHelper.clamp(index, 0, getText().length());
	}


	@Override
	public String getFormattedValue() {
		return getValue() == null ? "" : getValue();
	}

	private int renderHeightPreview() {
		String value = getValue() == null ? "" : getValue();
		int lineHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 1;
		int lines = TextUtil.wrapText(value, width - 4).size();
		return Math.max(lineHeight, lines * lineHeight) + 2;
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("value", getValue());
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		setValue(jsonObject.get("value").getAsString());
	}
}
