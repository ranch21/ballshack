package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingColor;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.util.Arrays;
import java.util.List;

/**
 * Client module that holds color override settings.
 * Has a single boolean to enable overriding all colors.
 */
public class ColorsConfig extends Module {

	public ColorsConfig() {
		// Create settings inline to avoid referencing fields before super()
		super("Colors", ModuleCategory.CLIENT, 0, new ModuleSettings(Arrays.asList(
				new DropDown("Override", List.of(
						new SettingToggle(false, "OverrideAll") {
							@Override
							public void setValue(Boolean value) {
								super.setValue(value);
								// Update global Colors when toggles might affect overrides
								Colors.refreshFromSettings();
							}
						}
				)),
				new DropDown("Palettes", Arrays.asList(
						new SettingColor("Palette1", Colors.DEFAULT_PALLETE_1),
						new SettingColor("Palette2", Colors.DEFAULT_PALLETE_2),
						new SettingColor("Palette3", Colors.DEFAULT_PALLETE_3),
						new SettingColor("Palette4", Colors.DEFAULT_PALLETE_4),
						new SettingColor("Palette5", Colors.DEFAULT_PALLETE_5)
				)),
				new DropDown("ClickGUI", Arrays.asList(
						new SettingColor("TitleStart", Colors.DEFAULT_CLICKGUI_TITLE_START),
						new SettingColor("TitleEnd", Colors.DEFAULT_CLICKGUI_TITLE_END),
						new SettingColor("Click2", Colors.DEFAULT_CLICKGUI_2),
						new SettingColor("Click3", Colors.DEFAULT_CLICKGUI_3),
						new SettingColor("ClickBg", Colors.DEFAULT_CLICKGUI_BACKGROUND)
				)),
				new DropDown("Entities", Arrays.asList(
						new SettingColor("Warn", Colors.DEFAULT_WARN),
						new SettingColor("Hostile", Colors.DEFAULT_HOSTILE),
						new SettingColor("Passive", Colors.DEFAULT_PASSIVE),
						new SettingColor("Player", Colors.DEFAULT_PLAYER),
						new SettingColor("Else", Colors.DEFAULT_ELSE)
				)),
				new DropDown("Utility", Arrays.asList(
						new SettingColor("Red", Colors.DEFAULT_RED),
						new SettingColor("Green", Colors.DEFAULT_GREEN),
						new SettingColor("Blue", Colors.DEFAULT_BLUE),
						new SettingColor("Gray", Colors.DEFAULT_GRAY),
						new SettingColor("Backdrop", Colors.DEFAULT_BACKDROP),
						new SettingColor("Selectable", Colors.DEFAULT_SELECTABLE),
						new SettingColor("Border", Colors.DEFAULT_BORDER)
				))
		)), "Customize Colors", true);
	}
}
