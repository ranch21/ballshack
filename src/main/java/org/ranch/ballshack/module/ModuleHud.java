package org.ranch.ballshack.module;

import org.ranch.ballshack.setting.ModuleSettings;

public class ModuleHud extends Module {

	public int x, y;

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y) {
		super(name, category, bind);
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, ModuleSettings settings) {
		super(name, category, bind, settings);
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}
}
