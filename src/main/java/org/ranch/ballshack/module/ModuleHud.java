package org.ranch.ballshack.module;

import org.ranch.ballshack.setting.ModuleSettings;

public class ModuleHud extends Module {

	protected int x;
	protected int y;

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y) {
		super(name, category, bind);
		this.x = x;
		this.y = y;
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, ModuleSettings settings) {
		super(name, category, bind, settings);
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
