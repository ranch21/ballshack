package org.ranch.ballshack.module;

import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.setting.HudElementData;
import org.ranch.ballshack.setting.HudModuleSettings;
import org.ranch.ballshack.util.DrawUtil;

public class ModuleHud extends Module {

	public int offsetx, offsety;
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

	public int X() {
		return anchorPoint.getX(DrawUtil.getScreenWidth()) + offsetx;
	}

	public int Y() {
		return anchorPoint.getY(DrawUtil.getScreenHeight()) + offsety;
	}

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, HudModuleSettings settings, @Nullable String tooltip, ModuleAnchor anchorPoint) {
		super(name, category, bind, settings, tooltip);
		this.offsetx = x;
		this.offsety = y;
		this.width = 10;
		this.height = 10;
		this.anchorPoint = anchorPoint;
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
		HudElementData hdata = ((HudModuleSettings)settings).getHudSetting().getValue();
		hdata.x = offsetx;
		hdata.y = offsety;
		((HudModuleSettings)settings).getHudSetting().setValue(hdata);
	}

	public void setAnchorPoint(ModuleAnchor anchorPoint) {
		this.anchorPoint = anchorPoint;
		HudElementData hdata = ((HudModuleSettings)settings).getHudSetting().getValue();
		hdata.anchor = anchorPoint;
		((HudModuleSettings)settings).getHudSetting().setValue(hdata);
	}

	public ModuleAnchor getAnchorPoint() {
		return anchorPoint;
	}

	public boolean isOnRight() {
		return anchorPoint.isRight();
	}
}
