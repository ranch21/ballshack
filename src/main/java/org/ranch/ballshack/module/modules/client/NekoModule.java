package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

import static org.ranch.ballshack.gui.neko.NekoTextures.TEXTURE_SIZE;

public class NekoModule extends Module {
	public NekoModule() {
		super("Neko", ModuleCategory.CLIENT, 0, "cat");
	}

	public final SettingSlider size = dGroup.add(new SettingSlider("Size", TEXTURE_SIZE, TEXTURE_SIZE / 4, TEXTURE_SIZE * 1.5, TEXTURE_SIZE / 4));

	@Override
	public void onEnable() {
		super.onEnable();
		BallsHack.eventBus.subscribe(BallsHack.neko);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		BallsHack.eventBus.unsubscribe(BallsHack.neko);
	}
}
