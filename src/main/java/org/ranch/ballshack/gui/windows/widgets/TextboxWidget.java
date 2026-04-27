package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.util.TextUtil;

import java.awt.*;
import java.util.List;

public class TextboxWidget extends Widget {

	private String value;
	private int maxLength;

	private int selectionStart;
	private int selectionEnd;

	public TextboxWidget(String title, int x, int y, int width, int height) {
		super(title, x, y, width, height);
		addFlags(INDENTED);
		value = "";
	}

	@Override
	public void init() {
		super.init();
		List<String> splitLines = TextUtil.wrapText(value, getWidth() - 4);
		setHeight(Math.max(mc.textRenderer.fontHeight, splitLines.size() * mc.textRenderer.fontHeight + 1 + 1));
		setSelection();
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		String value = getText();
		if (isFocused()) {
			value = new StringBuilder(value).insert(selectionStart, "_").toString();
		}

		List<String> splitLines = TextUtil.wrapText(value, getWidth() - 4);

		int lineHeight = mc.textRenderer.fontHeight + 1;

		int i = 0;
		for (String line : splitLines) {
			text(line, 2, i++ * lineHeight + 1, Color.WHITE.hashCode(), true);
		}
		setHeight(Math.max(mc.textRenderer.fontHeight, i * lineHeight + 1));
	}

	private void setSelection() {
		selectionStart = getText().length();
		selectionEnd = selectionStart;
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		return super.mouseClicked(click, doubled);
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		if (focused) {
			setSelection();
		}
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (isFocused()) {
			boolean shift = (input.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0;

			if (input.getKeycode() == GLFW.GLFW_KEY_ENTER) {
				if (shift) {
					write("\n");
				}
				return true;
			}
			if (input.getKeycode() == GLFW.GLFW_KEY_BACKSPACE) {
				erase();
			}
			if (input.getKeycode() == GLFW.GLFW_KEY_LEFT) {
				moveCursor(-1);
			}
			if (input.getKeycode() == GLFW.GLFW_KEY_RIGHT) {
				moveCursor(1);
			}
		}

		return super.keyPressed(input);
	}

	@Override
	public boolean charTyped(CharInput input) {
		if (StringHelper.isValidChar(input.codepoint()) && isFocused()) {
			write(Character.toString(input.codepoint()));
			return true;
		}
		return super.charTyped(input);
	}

	public String getText() {
		return value == null ? "" : value;
	}

	public void write(String text) {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		int k = maxLength - getText().length() - (i - j);
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
			value = string2;
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
					value = string;
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
}
