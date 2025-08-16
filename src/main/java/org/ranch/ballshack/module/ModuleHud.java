package org.ranch.ballshack.module;

import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.setting.ModuleSettings;

import java.util.ArrayList;

public class ModuleHud extends Module {

	public int x, y;

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y) {
		this(name, category, bind, x, y, new ModuleSettings(new ArrayList<>()), null);
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, String tooltip) {
		this(name, category, bind, x, y, new ModuleSettings(new ArrayList<>()), tooltip);
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, ModuleSettings settings) {
		this(name, category, bind, x, y, settings, null);
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, ModuleSettings settings, @Nullable String tooltip) {
		super(name, category, bind, settings, tooltip);
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isOnRight() {
		return x > mc.getWindow().getScaledWidth() / 2;
	}
}
