package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.HudScreen;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.setting.HudElementData;
import org.ranch.ballshack.setting.ModuleSetting;

import static org.ranch.ballshack.BallsHack.mc;

public class SettingHud extends ModuleSetting<HudElementData, SettingHud> {

	public SettingHud(HudElementData startingValue) {
		super("HudData", startingValue);
	}

	@Override
	public int render(int mouseX, int mouseY) {

		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

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
		return "x: " + value.x + " y: " + value.y/* + " a: " + value.anchor*/;
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("x", getValue().x);
		obj.addProperty("y", getValue().y);
		obj.addProperty("anchor", getValue().anchor.ordinal());
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		int x = jsonObject.get("x").getAsInt();
		int y = jsonObject.get("y").getAsInt();
		int anchor = jsonObject.get("anchor").getAsInt();
		setValue(new HudElementData(x, y, ModuleAnchor.values()[anchor]));
	}
}