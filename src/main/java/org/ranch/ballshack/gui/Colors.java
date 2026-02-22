package org.ranch.ballshack.gui;

import java.awt.*;

public class Colors {

	// wgat a mess
	public static final ThemeManager.SavedColor PALETTE_1 = new ThemeManager.SavedColor(new Color(199, 163, 212), "palette_1");
	public static final ThemeManager.SavedColor PALETTE_2 = new ThemeManager.SavedColor(new Color(157, 116, 176), "palette_2");

	public static final ThemeManager.SavedColor HUD_BACKGROUND = new ThemeManager.SavedColor(new Color(0, 0, 0, 50), "hud_background");

	public static final ThemeManager.SavedColor TITLE_START = new ThemeManager.SavedColor(PALETTE_1.defaultCol(), "title_start");
	public static final ThemeManager.SavedColor TITLE_END = new ThemeManager.SavedColor(PALETTE_2.defaultCol(), "title_end");

	public static final ThemeManager.SavedColor FILL = new ThemeManager.SavedColor(new Color(122, 92, 141, 150), "fill");

	public static final ThemeManager.SavedColor BORDER_TOP = new ThemeManager.SavedColor(Color.WHITE, "border_top");
	public static final ThemeManager.SavedColor BORDER_BOTTOM = new ThemeManager.SavedColor(Color.WHITE, "border_bottom");

	public static final ThemeManager.SavedColor SELECTABLE = new ThemeManager.SavedColor(PALETTE_2.defaultCol(), "selectable");
	public static final ThemeManager.SavedColor SELECTED = new ThemeManager.SavedColor(PALETTE_2.defaultCol(), "selected");

	public static final Color DULL_RED = new Color(255, 124, 119, 255);
	public static final Color DULL_GREEN = new Color(184, 255, 171, 255);
	public static final Color DULL_BLUE = new Color(158, 178, 255, 255);
	public static final Color DULL_GRAY = new Color(170, 170, 170, 255);

	public static final Color[] INVENTORY_COLORS = new Color[]{
			Color.ORANGE, // CHEST
			Color.MAGENTA, // ECHEST
			new Color(200, 50, 100, 255), // TCHEST
			Color.GRAY, // FURNACE
			Color.PINK, // SHULKER
			new Color(160, 82, 45) // BARREL
	};

	public static float globalRainbowSpeed = 1.0f;
	public static float globalRainbowSaturation = 0.3f;
	public static float globalRainbowBrightness = 1f;

	public static Color getRainbowColor(float tick, float speed, float saturation, float brightness) {
		float hue = (float) ((Math.ceil(tick * speed) % 360) / 360f);
		return Color.getHSBColor(hue, saturation, brightness);
	}

	public static Color getRainbowColorGlobal(float tick) {
		return getRainbowColor(tick, globalRainbowSpeed, globalRainbowSaturation, globalRainbowBrightness);
	}
}
