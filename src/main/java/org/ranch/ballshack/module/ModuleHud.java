package org.ranch.ballshack.module;

import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.setting.module.HudElementData;
import org.ranch.ballshack.setting.module.settings.HudSetting;
import org.ranch.ballshack.util.rendering.DrawUtil;

public class ModuleHud extends Module {

	//public int originx, originy;
	public int width, height;

	private final HudSetting hsetting = dGroup.add(new HudSetting("Hud data", new HudElementData(0, 0, ModuleAnchor.TOP_LEFT)));

	public ModuleHud(String name, ModuleCategory category, int bind, int x, int y, @Nullable String tooltip, ModuleAnchor anchorPoint) {
		super(name, category, bind, tooltip);
		hsetting.getValue().x = x;
		hsetting.getValue().y = y;
		this.width = 10;
		this.height = 10;
		hsetting.getValue().anchor = anchorPoint;
	}

	public int X() {
		return hsetting.getValue().anchor.getX(DrawUtil.getScreenWidth()) + hsetting.getValue().x;
	}

	public int Y() {
		return hsetting.getValue().anchor.getY(DrawUtil.getScreenHeight()) + hsetting.getValue().y;
	}

	public int xOffset() {
		int off;
		if (hsetting.getValue().anchor.isCenter()) off = getWidth() / 2;
		else if (hsetting.getValue().anchor.isRight()) off = getWidth();
		else off = 0;
		return off;
	}

	public int yOffset() {
		int off;
		if (hsetting.getValue().anchor.isBottom()) off = getHeight();
		else if (hsetting.getValue().anchor.isTop()) off = 0;
		else if (hsetting.getValue().anchor.isCenter()) off = getHeight() / 2;
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
		HudElementData hdata = hsetting.getValue();

		hdata.x = x - hdata.anchor.getX(DrawUtil.getScreenWidth());
		hdata.y = y - hdata.anchor.getY(DrawUtil.getScreenHeight());
		//hsetting.setValue(hdata);
	}

	public void setAnchorPoint(ModuleAnchor anchorPoint) {
		HudElementData hdata = hsetting.getValue();
		hdata.anchor = anchorPoint;
		//hsetting.setValue(hdata);
	}

	public ModuleAnchor getAnchorPoint() {
		return hsetting.getValue().anchor;
	}

	public boolean isOnRight() {
		return hsetting.getValue().anchor.isRight();
	}
}
