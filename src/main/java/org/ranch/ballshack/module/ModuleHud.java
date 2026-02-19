package org.ranch.ballshack.module;

import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.setting.HudElementData;
import org.ranch.ballshack.util.rendering.DrawUtil;

public class ModuleHud extends Module {

	public int offsetx, offsety;
	//public int originx, originy;
	public int width, height;
	private ModuleAnchor anchorPoint;

	//private final SettingHud hsetting = settings.add(new SettingHud(new HudElementData(0, 0, ModuleAnchor.TOP_LEFT)));

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, @Nullable String tooltip, ModuleAnchor anchorPoint) {
		super(name, category, bind, tooltip);
		this.offsetx = x;
		this.offsety = y;
		this.width = 10;
		this.height = 10;
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
		//HudElementData hdata = hsetting.getValue();
		//hdata.x = offsetx;
		//hdata.y = offsety;
		//hsetting.setValue(hdata);
	}

	public void setAnchorPoint(ModuleAnchor anchorPoint) {
		this.anchorPoint = anchorPoint;
		//HudElementData hdata = hsetting.getValue();
		//hdata.anchor = anchorPoint;
		//hsetting.setValue(hdata);
	}

	public ModuleAnchor getAnchorPoint() {
		return anchorPoint;
	}

	public boolean isOnRight() {
		return anchorPoint.isRight();
	}
}
