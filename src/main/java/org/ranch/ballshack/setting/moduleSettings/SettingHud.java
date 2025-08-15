package org.ranch.ballshack.setting.moduleSettings;

import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.HudScreen;
import org.ranch.ballshack.setting.HudElementData;
import org.ranch.ballshack.setting.ModuleSetting;

import static org.ranch.ballshack.BallsHack.mc;

public class SettingHud extends ModuleSetting<HudElementData> {
	public SettingHud(HudElementData startingValue) {
		super("HudData", startingValue);
	}

	@Override
	public int render(int mouseX, int mouseY) {

		context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.hashCode());

		/* setting WRHOIBNG and value */
		HudElementData hudData = getValue();
		drawText(context, "x: " + hudData.x + " y: " + hudData.y + " a: " + hudData.anchor);
		//drawTextRightAligned(context, this.getValue() ? "[#]" : "[ ]");

		return height;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			HudScreen screen = new HudScreen();
			mc.setScreen(screen);
			return true;
		}
		return false;
	}

	@Override
	public String getFormattedValue() {
		return "x: " + value.x + " y: " + value.y + " a: " + value.anchor;
	}
}