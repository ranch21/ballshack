package org.ranch.ballshack.setting.moduleSettings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.StringHelper;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Multi-line text setting with soft wrapping.
 * <p>
 * - Click to focus.
 * - Type to insert characters.
 * - Backspace deletes last character.
 * - ESC unfocuses.
 * - Shift+Enter inserts a real newline character ("\n").
 * - When text exceeds the width, it wraps visually (soft wrap) without adding characters.
 */
public class SettingString extends ModuleSetting<String> {

	private boolean focused = false;
	private int maxLength = 256;
	private long caretBlink = 0;

	public SettingString(String name, String startingValue) {
		super(name, startingValue);
	}

	public SettingString(String name, String startingValue, int maxLength) {
		super(name, startingValue);
		this.maxLength = Math.max(1, maxLength);
	}

	@Override
	public int render(int mouseX, int mouseY) {
		// background for the setting header row
		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

		// draw label on the header row
		drawText(context, this.getName() + ": ");

		// Prepare text to render (include caret for visual feedback)
		String value = getValue() == null ? "" : getValue();
		boolean caret = caretBlink / 60 % 2 == 0 && focused;
		caretBlink++;
		String displayValue = caret ? value + "|" : value;

		// Calculate wrapped lines for display beneath the header row
		List<String> visualLines = wrapText(displayValue, width - 4);

		// Draw a subtle box for the text area (optional, but helps clarity)
		int textAreaY = y + height; // render below the header row
		int lineHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 1;
		int textAreaHeight = Math.max(lineHeight, visualLines.size() * lineHeight) + 2;
		context.fill(x, textAreaY, x + width, textAreaY + textAreaHeight, Colors.CLICKGUI_3.hashCode());
		DrawUtil.drawOutline(context, x, textAreaY, width, textAreaHeight - 1);

		// Render each visual line inside the text area
		TextRenderer tr = MinecraftClient.getInstance().textRenderer;
		int ty = textAreaY + 1;
		for (String line : visualLines) {
			DrawUtil.drawText(context, tr, line, x + 2, ty, Color.WHITE, true);
			ty += lineHeight;
		}

		return height + textAreaHeight; // header height + text area height
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			focused = true;
			return true;
		} else {
			focused = false;
		}
		// Also allow clicking on the text area to focus
		int textAreaY = y + height;
		int totalHeight = renderHeightPreview();
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, textAreaY, width, totalHeight) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			focused = true;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		// keep focus until ESC or different widget logic handles blur
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
				appendChar('\n'); // hard newline
			} else {
				// Enter without shift: finish editing (unfocus)
				focused = false;
			}
			return;
		}
		if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
			String cur = getValue() == null ? "" : getValue();
			if (!cur.isEmpty()) {
				setValue(cur.substring(0, cur.length() - 1));
			}
		}
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (!focused) {
			return false;
		}
		// Do not accept raw Enter here; it's handled in keyPressed with shift detection
		if (chr == '\r' || chr == '\n') return true;

		if (StringHelper.isValidChar(chr)) {
			appendChar(chr);
			return true;
		}
		return false;
	}

	private void appendChar(char c) {
		String cur = getValue() == null ? "" : getValue();
		if (cur.length() >= maxLength) return;
		// Allow newline inserted via Shift+Enter only (we call appendChar('\n') there). If some other control char, ignore.
		if (c != '\n' && (c < 32 || c == 127)) return;
		setValue(cur + c);
	}

	@Override
	public String getFormattedValue() {
		return getValue() == null ? "" : getValue();
	}

	// Compute visual line wrapping without mutating the underlying string (soft wrap)
	private List<String> wrapText(String text, int maxPixelWidth) {
		TextRenderer tr = MinecraftClient.getInstance().textRenderer;
		List<String> out = new ArrayList<>();
		if (text == null || text.isEmpty()) {
			out.add("");
			return out;
		}

		String[] hardLines = text.split("\\n", -1);
		for (String hard : hardLines) {
			if (tr.getWidth(hard) <= maxPixelWidth) {
				out.add(hard);
				continue;
			}
			// soft-wrap this hard line
			int start = 0;
			int len = hard.length();
			while (start < len) {
				int end = findWrapPoint(hard, start, maxPixelWidth, tr);
				out.add(hard.substring(start, end));
				start = end;
			}
		}
		return out;
	}

	private int findWrapPoint(String s, int start, int maxPixelWidth, TextRenderer tr) {
		int end = start;
		int lastSpace = -1;
		while (end < s.length()) {
			char c = s.charAt(end);
			if (Character.isWhitespace(c)) {
				lastSpace = end;
			}
			String candidate = s.substring(start, end + 1);
			if (tr.getWidth(candidate) > maxPixelWidth) {
				// exceed width: prefer last space for wrap; if none, hard cut before current char
				if (lastSpace >= start) {
					return Math.max(lastSpace + 1, start + 1);
				}
				return Math.max(end, start + 1);
			}
			end++;
		}
		return end; // whole remainder fits
	}

	// A small helper to estimate current text area height for hit testing without full render duplication
	private int renderHeightPreview() {
		String value = getValue() == null ? "" : getValue();
		String displayValue = focused ? value + "|" : value;
		int lineHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 1;
		int lines = wrapText(displayValue, width - 4).size();
		return Math.max(lineHeight, lines * lineHeight) + 2;
	}
}
