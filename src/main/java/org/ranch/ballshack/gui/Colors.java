package org.ranch.ballshack.gui;

import java.awt.*;

public class Colors {
	/*public static final Color PALLETE_1 = new Color(22, 219, 101);
	public static final Color PALLETE_2 = new Color(5, 140, 66);
	public static final Color PALLETE_3 = new Color(4, 71, 28);
	public static final Color PALLETE_4 = new Color(13, 40, 24);
	public static final Color PALLETE_5 = new Color(2, 2, 2);

	public static final Color CLICKGUI_TITLE_START = PALLETE_1;
	public static final Color CLICKGUI_TITLE_END = PALLETE_2;
	public static final Color CLICKGUI_2 = new Color(50, 50, 50, 200);
	public static final Color CLICKGUI_3 = new Color(0, 0, 0, 150);*/

	public static final Color PALLETE_1 = new Color(199,163,212);
	public static final Color PALLETE_2 = new Color(157,116,176);
	public static final Color PALLETE_3 = new Color(4, 71, 28);
	public static final Color PALLETE_4 = new Color(13, 40, 24);
	public static final Color PALLETE_5 = new Color(2, 2, 2);

	public static final Color CLICKGUI_TITLE_START = PALLETE_1;
	public static final Color CLICKGUI_TITLE_END = PALLETE_2;
	public static final Color CLICKGUI_2 = new Color(122,92,141, 150);
	public static final Color CLICKGUI_3 = new Color(90,60,100, 100);
	public static final Color CLICKGUI_BACKGROUND = new Color(50,20,70, 60);

	public static final Color[] CATEGORY_COLORS = new Color[] {
		Color.BLUE, // PLAYER
		Color.YELLOW, // RENDER
		Color.RED, // COMBAT
		Color.MAGENTA, // MOVEMENT
		Color.ORANGE, // WORLD
		Color.GREEN, // FUN
	};

	public static final Color[] INVENTORY_COLORS = new Color[] {
			Color.ORANGE, // CHEST
			Color.MAGENTA, // ECHEST
			new Color(200, 50, 100, 255), // TCHEST
			Color.GRAY, // FURNACE
			Color.PINK, // SHULKER
			new Color(160, 82, 45) // BARREL
	};

	public static final Color HOSTILE = new Color(255, 0, 0, 250);
	public static final Color PASSIVE = new Color(0, 255, 0, 250);
	public static final Color PLAYER = new Color(0, 0, 255, 250);
	public static final Color ELSE = new Color(200, 200, 200, 250);

	//public static final Color CHEST = new Color(255, 255, 200, 250);

	public static final Color BACKDROP = new Color(0, 0, 0, 50);

	public static final Color SELECTABLE = PALLETE_2;

	public static final Color BORDER = Color.WHITE;

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
