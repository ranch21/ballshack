package org.ranch.ballshack.gui;

import java.awt.*;

public class Colors {

	// wgat a mess
	public static final ThemeManager.SavedColor PALETTE_1 = new ThemeManager.SavedColor(new Color(199, 163, 212), "palette_1");
	public static final ThemeManager.SavedColor PALETTE_2 = new ThemeManager.SavedColor(new Color(157, 116, 176), "palette_2");

	public static final ThemeManager.SavedColor CLICKGUI_1 = new ThemeManager.SavedColor(new Color(122, 92, 141, 150), "clickgui_1");
	public static final ThemeManager.SavedColor CLICKGUI_2 = new ThemeManager.SavedColor(new Color(90, 60, 100, 100), "clickgui_2");
	public static final ThemeManager.SavedColor CLICKGUI_BACKGROUND_1 = new ThemeManager.SavedColor(new Color(50, 20, 70, 60), "clickgui_bg_1");
	public static final ThemeManager.SavedColor CLICKGUI_BACKGROUND_2 = new ThemeManager.SavedColor(new Color(0, 0, 0, 50), "clickgui_bg_2");

	public static final ThemeManager.SavedColor CLICKGUI_TITLE_START = new ThemeManager.SavedColor(PALETTE_1.defaultCol(), "title_start");
	public static final ThemeManager.SavedColor CLICKGUI_TITLE_END = new ThemeManager.SavedColor(PALETTE_2.defaultCol(), "title_end");;

	public static final ThemeManager.SavedColor BORDER = new ThemeManager.SavedColor(Color.WHITE, "border");

	public static final ThemeManager.SavedColor SELECTABLE = new ThemeManager.SavedColor(PALETTE_2.defaultCol(), "selectable");

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
