package org.ranch.ballshack.module;

import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.setting.HudElementData;
import org.ranch.ballshack.setting.HudModuleSettings;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.util.ArrayList;

public class ModuleHud extends Module {

	public int offsetx, offsety;
	//public int originx, originy;
	public int width, height;
	private ModuleAnchor anchorPoint;

	/*public ModuleHud(String name, ModuleCategory category, int bind, int x, int y) {
		this(name, category, bind, x, y, new ModuleSettings(new ArrayList<>()), null);
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, String tooltip) {
		this(name, category, bind, x, y, new ModuleSettings(new ArrayList<>()), tooltip);
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, ModuleSettings settings) {
		this(name, category, bind, x, y, settings, null);
	}*/

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, @Nullable String tooltip, ModuleAnchor anchorPoint) {
		super(name, category, bind, tooltip);
		this.offsetx = x;
		this.offsety = y;
		this.width = 10;
		this.height = 10;
		//this.originx = 0;
		//this.originy = 0;
		this.anchorPoint = anchorPoint;
	}

	public int X() {
		return anchorPoint.getX(DrawUtil.getScreenWidth()) + offsetx;
	}

	public int Y() {
		return anchorPoint.getY(DrawUtil.getScreenHeight()) + offsety;
	}

	public int xOffset() {
		int off;
		if (anchorPoint.isCenter()) off = getWidth() / 2;
		else if (anchorPoint.isRight()) off = getWidth();
		else off = 0;
		return off;
	}

	public int yOffset() {
		int off;
		if (anchorPoint.isBottom()) off = getHeight();
		else if (anchorPoint.isTop()) off = 0;
		else if (anchorPoint.isCenter()) off = getHeight() / 2;
		else off = 0;
		return off;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setPos(int x, int y) {
		this.offsetx = x - anchorPoint.getX(DrawUtil.getScreenWidth());
		this.offsety = y - anchorPoint.getY(DrawUtil.getScreenHeight());
		HudElementData hdata = ((HudModuleSettings) settings).getHudSetting().getValue();
		hdata.x = offsetx;
		hdata.y = offsety;
		((HudModuleSettings) settings).getHudSetting().setValue(hdata);
	}

	public void setAnchorPoint(ModuleAnchor anchorPoint) {
		this.anchorPoint = anchorPoint;
		HudElementData hdata = ((HudModuleSettings) settings).getHudSetting().getValue();
		hdata.anchor = anchorPoint;
		((HudModuleSettings) settings).getHudSetting().setValue(hdata);
	}

	public ModuleAnchor getAnchorPoint() {
		return anchorPoint;
	}

	public boolean isOnRight() {
		return anchorPoint.isRight();
	}
}
