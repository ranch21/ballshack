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
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.util.Arrays;

public class ClickGui extends Module {

	private ClickGuiScreen screen;
	private boolean legacy;

	public ClickGui() {
		super("ClickGui", ModuleCategory.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT, new ModuleSettings(Arrays.asList(
				new DropDown("Balls", Arrays.asList(
						new SettingToggle(true, "Enabled"),
						new SettingSlider(50,"Amount", 1, 250, 5),
						new SettingSlider(1,"Gravity", 0.25, 10, 0.25),
						new SettingSlider(0.6,"Bounce", 0.25, 4, 0.1)
				)),
				new SettingToggle(true, "Darken"),
				new SettingToggle(false, "Legacy")
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		legacy = (boolean) settings.getSetting(2).getValue();
		screen.setSettings(this.getSettings());
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (legacy) {
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
