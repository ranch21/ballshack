package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

public class SettingToggle extends ModuleSetting<Boolean, SettingToggle> {

	public SettingToggle(String name, boolean startingValue) {
		super(name, startingValue);
	}

	@Override
	public int render(int mouseX, int mouseY) {

		if (this.getValue()) {
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.brighter().hashCode());
		} else {
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());
		}

		/* setting name and value */
		drawText(context, this.getName());
		drawTextRightAligned(context, this.getValue() ? "[#]" : "[ ]");

		return height;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			this.setValue(!this.getValue());
			return true;
		}
		return false;
	}

	@Override
	public String getFormattedValue() {
		return String.valueOf(value);
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("value", getValue());
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		setValue(jsonObject.get("value").getAsBoolean());
	}
}
