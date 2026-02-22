package org.ranch.ballshack.module.modules.client;

import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.settings.NumberSetting;

import static org.ranch.ballshack.gui.neko.NekoTextures.TEXTURE_SIZE;

public class NekoModule extends Module {

	public final NumberSetting size = dGroup.add(new NumberSetting("Size", TEXTURE_SIZE)
			.min((double) TEXTURE_SIZE / 4).max(TEXTURE_SIZE * 2).step((double) TEXTURE_SIZE / 4));

	public NekoModule() {
		super("Neko", ModuleCategory.CLIENT, 0, "cat");
	}

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