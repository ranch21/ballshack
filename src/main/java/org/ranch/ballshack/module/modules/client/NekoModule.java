package org.ranch.ballshack.module.modules.client;

import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class NekoModule extends Module {
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
