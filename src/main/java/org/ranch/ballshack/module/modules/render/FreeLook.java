package org.ranch.ballshack.module.modules.render;

import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.util.FreelookHandler;

public class FreeLook extends Module {

	public FreeLook() {
		super("FreeLook", ModuleCategory.RENDER, 0, "goteteds");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		FreelookHandler.enable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		FreelookHandler.disable();
	}
}
