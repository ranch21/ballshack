package org.ranch.ballshack.gui;

import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingColor;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.awt.*;

public class Colors {
	// Defaults preserved
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
	//public static final Color DEFAULT_CHEST = new Color(255, 255, 200, 250);

	public static final Color DEFAULT_BACKDROP = new Color(0, 0, 0, 50);

	public static final Color DEFAULT_SELECTABLE = DEFAULT_PALLETE_2;

	public static final Color DEFAULT_BORDER = Color.WHITE;

	// Effective colors (used everywhere in GUI/rendering). These can be overridden.
	public static Color PALLETE_1 = DEFAULT_PALLETE_1;
	public static Color PALLETE_2 = DEFAULT_PALLETE_2;
	public static Color PALLETE_3 = DEFAULT_PALLETE_3;
	public static Color PALLETE_4 = DEFAULT_PALLETE_4;
	public static Color PALLETE_5 = DEFAULT_PALLETE_5;

	public static Color CLICKGUI_TITLE_START = DEFAULT_CLICKGUI_TITLE_START;
	public static Color CLICKGUI_TITLE_END = DEFAULT_CLICKGUI_TITLE_END;
	public static Color CLICKGUI_2 = DEFAULT_CLICKGUI_2;
	public static Color CLICKGUI_3 = DEFAULT_CLICKGUI_3;
	public static Color CLICKGUI_BACKGROUND = DEFAULT_CLICKGUI_BACKGROUND;

	public static Color WARN = DEFAULT_WARN;
	public static Color HOSTILE = DEFAULT_HOSTILE;
	public static Color PASSIVE = DEFAULT_PASSIVE;
	public static Color PLAYER = DEFAULT_PLAYER;
	public static Color ELSE = DEFAULT_ELSE;

	public static Color RED = DEFAULT_RED;
	public static Color GREEN = DEFAULT_GREEN;
	public static Color BLUE = DEFAULT_BLUE;
	public static Color GRAY = DEFAULT_GRAY;
	public static Color BACKDROP = DEFAULT_BACKDROP;
	public static Color SELECTABLE = DEFAULT_SELECTABLE;
	public static Color BORDER = DEFAULT_BORDER;

	public static float globalRainbowSpeed = 1.0f;
	public static float globalRainbowSaturation = 0.3f;
	public static float globalRainbowBrightness = 1f;

	private static boolean lastOverride = false;

	public static void refreshFromSettings() {
		Module m = ModuleManager.getModuleByName("Colors");
		if (m == null) {
			applyDefaults();
			return;
		}
		ModuleSettings ms = m.getSettings();
		// Get the override toggle from any nested setting
		boolean overrideAll = false;
		for (ModuleSetting<?> s : ms.getSettingsUnpacked()) {
			if (s.getName().equals("OverrideAll") && s instanceof SettingToggle tog) {
				overrideAll = tog.getValue();
				break;
			}
		}
		if (!overrideAll) {
			if (lastOverride) applyDefaults();
			lastOverride = false;
			return;
		}
		lastOverride = true;

		PALLETE_1 = getColor(ms, "Palette1", DEFAULT_PALLETE_1);
		PALLETE_2 = getColor(ms, "Palette2", DEFAULT_PALLETE_2);
		PALLETE_3 = getColor(ms, "Palette3", DEFAULT_PALLETE_3);
		PALLETE_4 = getColor(ms, "Palette4", DEFAULT_PALLETE_4);
		PALLETE_5 = getColor(ms, "Palette5", DEFAULT_PALLETE_5);

		CLICKGUI_TITLE_START = getColor(ms, "TitleStart", DEFAULT_CLICKGUI_TITLE_START);
		CLICKGUI_TITLE_END = getColor(ms, "TitleEnd", DEFAULT_CLICKGUI_TITLE_END);
		CLICKGUI_2 = getColor(ms, "Click2", DEFAULT_CLICKGUI_2);
		CLICKGUI_3 = getColor(ms, "Click3", DEFAULT_CLICKGUI_3);
		CLICKGUI_BACKGROUND = getColor(ms, "ClickBg", DEFAULT_CLICKGUI_BACKGROUND);

		WARN = getColor(ms, "Warn", DEFAULT_WARN);
		HOSTILE = getColor(ms, "Hostile", DEFAULT_HOSTILE);
		PASSIVE = getColor(ms, "Passive", DEFAULT_PASSIVE);
		PLAYER = getColor(ms, "Player", DEFAULT_PLAYER);
		ELSE = getColor(ms, "Else", DEFAULT_ELSE);

		RED = getColor(ms, "Red", DEFAULT_RED);
		GREEN = getColor(ms, "Green", DEFAULT_GREEN);
		BLUE = getColor(ms, "Blue", DEFAULT_BLUE);
		GRAY = getColor(ms, "Gray", DEFAULT_GRAY);
		BACKDROP = getColor(ms, "Backdrop", DEFAULT_BACKDROP);
		SELECTABLE = getColor(ms, "Selectable", DEFAULT_SELECTABLE);
		BORDER = getColor(ms, "Border", DEFAULT_BORDER);
	}

	private static Color getColor(ModuleSettings ms, String name, Color def) {
		for (ModuleSetting<?> s : ms.getSettingsUnpacked()) {
			if (s.getName().equals(name) && s instanceof SettingColor sc) {
				Color c = sc.getValue();
				return c != null ? c : def;
			}
		}
		return def;
	}

	public static void applyDefaults() {
		PALLETE_1 = DEFAULT_PALLETE_1;
		PALLETE_2 = DEFAULT_PALLETE_2;
		PALLETE_3 = DEFAULT_PALLETE_3;
		PALLETE_4 = DEFAULT_PALLETE_4;
		PALLETE_5 = DEFAULT_PALLETE_5;

		CLICKGUI_TITLE_START = DEFAULT_CLICKGUI_TITLE_START;
		CLICKGUI_TITLE_END = DEFAULT_CLICKGUI_TITLE_END;
		CLICKGUI_2 = DEFAULT_CLICKGUI_2;
		CLICKGUI_3 = DEFAULT_CLICKGUI_3;
		CLICKGUI_BACKGROUND = DEFAULT_CLICKGUI_BACKGROUND;

		WARN = DEFAULT_WARN;
		HOSTILE = DEFAULT_HOSTILE;
		PASSIVE = DEFAULT_PASSIVE;
		PLAYER = DEFAULT_PLAYER;
		ELSE = DEFAULT_ELSE;

		RED = DEFAULT_RED;
		GREEN = DEFAULT_GREEN;
		BLUE = DEFAULT_BLUE;
		GRAY = DEFAULT_GRAY;
		BACKDROP = DEFAULT_BACKDROP;
		SELECTABLE = DEFAULT_SELECTABLE;
		BORDER = DEFAULT_BORDER;
	}

	public static Color getRainbowColor(float tick, float speed, float saturation, float brightness) {
		float hue = (float) ((Math.ceil(tick * speed) % 360) / 360f);
		return Color.getHSBColor(hue, saturation, brightness);
	}

	public static Color getRainbowColorGlobal(float tick) {
		return getRainbowColor(tick, globalRainbowSpeed, globalRainbowSaturation, globalRainbowBrightness);
	}
}
