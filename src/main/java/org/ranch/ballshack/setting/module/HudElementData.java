package org.ranch.ballshack.setting.module;

import org.ranch.ballshack.module.ModuleAnchor;

public class HudElementData {
	public int x;
	public int y;
	public ModuleAnchor anchor;

	public HudElementData(int x, int y, ModuleAnchor anchor) {
		this.x = x;
		this.y = y;
		this.anchor = anchor;
	}
}