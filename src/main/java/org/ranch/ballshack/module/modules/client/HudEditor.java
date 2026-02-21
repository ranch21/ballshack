package org.ranch.ballshack.module.modules.client;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.gui.HudScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class HudEditor extends Module {

	public HudEditor() {
		super("HudEdit", ModuleCategory.CLIENT, 0, "hud edit wgat is it", true);
	}

	@Override
	public void onEnable() {
		super.onEnable();

		HudScreen screen = new HudScreen();
		MinecraftClient.getInstance().setScreen(screen);
	}

	@Override
	public void toggle() {
		onEnable();
	}
}
