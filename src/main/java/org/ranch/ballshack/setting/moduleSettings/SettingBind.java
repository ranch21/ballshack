package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

public class SettingBind extends ModuleSetting<Integer, SettingBind> {

	private boolean selected;

	public SettingBind(String name, Integer value) {
		super(name, value);
	}

	@Override
	public int render(int mouseX, int mouseY) {

		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

		/* setting name and value */
		drawText(context, this.getName() + ": ");

		drawTextRightAligned(context, getFormattedValue());

		return height;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			selected = !selected;
			return true;
		}
		return false;
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (selected) {
			if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
				this.setValue(0);
				selected = false;
			} else if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
				this.setValue(keyCode);
				selected = false;
			}
		}
	}

	@Override
	public String getFormattedValue() {
		String keyName = GLFW.glfwGetKeyName(this.getValue(), 0);
		if (this.getValue() == 0) {
			return "None";
		} else if (keyName == null) {
			return getValue().toString();
		} else {
			return keyName;
		}
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("value", getValue());
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		setValue(jsonObject.get("value").getAsInt());
	}
}
