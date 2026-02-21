package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.NoWorld;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.gui.windows.WindowScreen;
import org.ranch.ballshack.gui.windows.clickgui.ClickGuiScreen;
import org.ranch.ballshack.gui.legacy.LegacyClickGuiScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;

public class ClickGui extends Module {

	private ClickGuiScreen screen;

	public final BooleanSetting darken = dGroup.add(new BooleanSetting("Darken", true));
	public final BooleanSetting legacy = dGroup.add(new BooleanSetting("Legacy", false));

	public final NumberSetting scale = dGroup.add(new NumberSetting("Scale", 1).min(0.5).max(2).step(0.1));


	public ClickGui() {
		super("ClickGui", ModuleCategory.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT, "The module screeent thing", true);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (legacy.getValue()) {
			LegacyClickGuiScreen screen = new LegacyClickGuiScreen();
			MinecraftClient.getInstance().setScreen(screen);
		} else {
			ClickGuiScreen screen = new ClickGuiScreen(null);
			this.screen = screen;
			screen.setScale(ModuleManager.getModuleByClass(ClickGui.class).scale.getValueFloat());
			MinecraftClient.getInstance().setScreen(screen);
		}
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
