package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.ClickGuiScreen;
import org.ranch.ballshack.gui.legacy.LegacyClickGuiScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

public class ClickGui extends Module {

	private ClickGuiScreen screen;

	public final DropDown ballsDrop = dGroup.add(new DropDown("Balls"));
	public final SettingToggle bEnabled = ballsDrop.add(new SettingToggle("Enabled", true));
	public final SettingSlider bAmount = ballsDrop.add(new SettingSlider("Amount", 500, 1, 2000, 100));
	public final SettingSlider bGrav = ballsDrop.add(new SettingSlider("Gravity", 1, 0, 3, 0.1));
	public final SettingSlider bSize = ballsDrop.add(new SettingSlider("Size", 16, 1, 32, 1));
	public final SettingToggle bWinCollide = ballsDrop.add(new SettingToggle("WindowCollide", true));

	public final SettingToggle darken = dGroup.add(new SettingToggle("Darken", true));
	public final SettingToggle legacy = dGroup.add(new SettingToggle("Legacy", false));

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
