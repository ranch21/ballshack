package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.ClickGuiScreen;
import org.ranch.ballshack.gui.legacy.LegacyClickGuiScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.SettingsList;
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.util.Arrays;
import java.util.Set;

public class ClickGui extends Module {

	private ClickGuiScreen screen;

	public DropDown ballsDrop = dGroup.add(new DropDown("Balls"));
	public SettingToggle bEnabled = dGroup.add(new SettingToggle(true, "Enabled"));
	public SettingSlider bAmount = dGroup.add(new SettingSlider(50, "Amount", 1, 2000, 100));
	public SettingSlider bGrav = dGroup.add(new SettingSlider(1, "Gravity", 0, 3, 0.1));
	public SettingSlider bSize = dGroup.add(new SettingSlider(10, "Size", 1, 32, 1));
	public SettingToggle bWinCollide = dGroup.add(new SettingToggle(true, "WindowCollide"));

	public SettingToggle darken = dGroup.add(new SettingToggle(true, "Darken"));
	public SettingToggle legacy = dGroup.add(new SettingToggle(false, "Legacy"));

	public ClickGui() {
		super("ClickGui", ModuleCategory.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT, "The module screeent thing", true);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		screen.setSettings(this);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (legacy.getValue()) {
			LegacyClickGuiScreen screen = new LegacyClickGuiScreen();
			MinecraftClient.getInstance().setScreen(screen);
		} else {
			ClickGuiScreen screen = new ClickGuiScreen();
			this.screen = screen;
			MinecraftClient.getInstance().setScreen(screen);
		}
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
