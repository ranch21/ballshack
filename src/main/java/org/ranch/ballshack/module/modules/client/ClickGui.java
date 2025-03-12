package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.ClickGuiScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.DropDown;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.setting.settings.SettingToggle;

import java.util.Arrays;

public class ClickGui extends Module {

	private ClickGuiScreen screen;

	public ClickGui() {
		super("ClickGui", ModuleCategory.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT, new ModuleSettings(Arrays.asList(
				new DropDown("Balls", Arrays.asList(
						new SettingToggle(true, "Enabled"),
						new SettingSlider(50,"Amount", 1, 250, 5),
						new SettingSlider(1,"Gravity", 0.25, 10, 0.25),
						new SettingSlider(0.6,"Bounce", 0.25, 4, 0.1)
				)),
				new SettingToggle(true, "Darken")
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		screen.setSettings(this.getSettings());
	}

	@Override
	public void onEnable() {
		super.onEnable();
		ClickGuiScreen screen = new ClickGuiScreen();
		this.screen = screen;
		MinecraftClient.getInstance().setScreen(screen);
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
