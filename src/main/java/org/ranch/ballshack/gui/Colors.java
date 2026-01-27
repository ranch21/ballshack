package org.ranch.ballshack.gui;

import java.awt.*;

public class Colors {
	// wgat a mess
	public static final Color DEFAULT_PALLETE_1 = new Color(199, 163, 212);
	public static final Color DEFAULT_PALLETE_2 = new Color(157, 116, 176);
	public static final Color DEFAULT_PALLETE_3 = new Color(4, 71, 28);
	public static final Color DEFAULT_PALLETE_4 = new Color(13, 40, 24);
	public static final Color DEFAULT_PALLETE_5 = new Color(2, 2, 2);

	public static final Color DEFAULT_CLICKGUI_TITLE_START = DEFAULT_PALLETE_1;
	public static final Color DEFAULT_CLICKGUI_TITLE_END = DEFAULT_PALLETE_2;
	public static final Color DEFAULT_CLICKGUI_2 = new Color(122, 92, 141, 150);
	public static final Color DEFAULT_CLICKGUI_3 = new Color(90, 60, 100, 100);
	public static final Color DEFAULT_CLICKGUI_BACKGROUND = new Color(50, 20, 70, 60);

	public static final Color[] INVENTORY_COLORS = new Color[]{
			Color.ORANGE, // CHEST
			Color.MAGENTA, // ECHEST
			new Color(200, 50, 100, 255), // TCHEST
			Color.GRAY, // FURNACE
			Color.PINK, // SHULKER
			new Color(160, 82, 45) // BARREL
	};

	public static final Color DEFAULT_WARN = new Color(200, 50, 100, 255);
	public static final Color DEFAULT_HOSTILE = new Color(255, 0, 0, 255);
	public static final Color DEFAULT_PASSIVE = new Color(0, 255, 0, 255);
	public static final Color DEFAULT_PLAYER = new Color(0, 0, 255, 255);
	public static final Color DEFAULT_ELSE = new Color(200, 200, 200, 255);

	public static final Color DEFAULT_RED = new Color(255, 124, 119, 255);
	public static final Color DEFAULT_GREEN = new Color(184, 255, 171, 255);
	public static final Color DEFAULT_BLUE = new Color(158, 178, 255, 255);
	public static final Color DEFAULT_GRAY = new Color(170, 170, 170, 255);

	public static final Color DEFAULT_BACKDROP = new Color(0, 0, 0, 50);

	public static final Color DEFAULT_SELECTABLE = DEFAULT_PALLETE_2;

	public static final Color DEFAULT_BORDER = Color.WHITE;

	public static final Color PALLETE_1 = DEFAULT_PALLETE_1;
	public static final Color PALLETE_2 = DEFAULT_PALLETE_2;
	public static Color PALLETE_3 = DEFAULT_PALLETE_3;
	public static Color PALLETE_4 = DEFAULT_PALLETE_4;
	public static Color PALLETE_5 = DEFAULT_PALLETE_5;

	public static final Color CLICKGUI_TITLE_START = DEFAULT_CLICKGUI_TITLE_START;
	public static final Color CLICKGUI_TITLE_END = DEFAULT_CLICKGUI_TITLE_END;
	public static final Color CLICKGUI_2 = DEFAULT_CLICKGUI_2;
	public static final Color CLICKGUI_3 = DEFAULT_CLICKGUI_3;
	public static final Color CLICKGUI_BACKGROUND = DEFAULT_CLICKGUI_BACKGROUND;

	public static Color WARN = DEFAULT_WARN;
	public static Color HOSTILE = DEFAULT_HOSTILE;
	public static Color PASSIVE = DEFAULT_PASSIVE;
	public static Color PLAYER = DEFAULT_PLAYER;
	public static Color ELSE = DEFAULT_ELSE;

	public static final Color RED = DEFAULT_RED;
	public static final Color GREEN = DEFAULT_GREEN;
	public static final Color BLUE = DEFAULT_BLUE;
	public static final Color GRAY = DEFAULT_GRAY;
	public static final Color BACKDROP = DEFAULT_BACKDROP;
	public static final Color SELECTABLE = DEFAULT_SELECTABLE;
	public static final Color BORDER = DEFAULT_BORDER;

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
