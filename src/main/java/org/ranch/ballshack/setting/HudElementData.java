package org.ranch.ballshack.setting;

import org.ranch.ballshack.module.ModuleAnchor;

public class HudElementData {
	public int x;
	public int y;
	public ModuleAnchor anchor;

	public HudElementData() {}

	public HudElementData(int x, int y, ModuleAnchor anchor) {
		this.x = x;
		this.y = y;
		this.anchor = anchor;
	}
}