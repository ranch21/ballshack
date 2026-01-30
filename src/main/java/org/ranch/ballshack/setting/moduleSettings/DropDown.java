package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.SettingsList;

import java.util.List;

public class DropDown extends ModuleSetting<Boolean, DropDown> implements SettingsList {

	public final ModuleSettingsGroup settings;
	private int addedHeight = 0;

	public DropDown(String label) {
		super(label, false);
		settings = new ModuleSettingsGroup(label);
	}

	@Override
	public int render(int mouseX, int mouseY) {

		addedHeight = 0;

		if (this.getValue()) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				addedHeight += setting.renderBase(context, x + 2, y + height + addedHeight, width - 2, height, mouseX, mouseY);
			}

			int bY = y + height;

			context.drawVerticalLine(x, bY - 1, bY + addedHeight, Colors.CLICKGUI_2.getColor().hashCode()); // indent thinger
			context.drawVerticalLine(x + 1, bY - 1, bY + addedHeight, Colors.BORDER.getColor().hashCode());
		}

		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_2.getColor().hashCode());

		/* setting name and arrow */
		drawText(context, this.getName());
		drawTextRightAligned(context, getFormattedValue());

		return height + addedHeight;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.getValue()) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				setting.mouseClickedBase(mouseX, mouseY, button);
			}
		}

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			this.setValue(!this.getValue());
			return true;

		} else
			return GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height + addedHeight) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (this.getValue()) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				setting.mouseReleasedBase(mouseX, mouseY, button);
			}
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.getValue()) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				setting.keyPressedBase(keyCode, scanCode, modifiers);
			}
		}
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (this.getValue()) {
			for (ModuleSetting<?, ?> setting : settings.getSettings()) {
				if (setting.charTypedBase(chr, modifiers)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getFormattedValue() {
		return this.getValue() ? "-" : "+";
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		for (ModuleSetting<?, ?> setting : settings.getSettings()) {
			obj.add(setting.getName(), setting.getJson());
		}
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		for (ModuleSetting<?, ?> setting : settings.getSettings()) {
			if (jsonObject.has(setting.getName())) {
				JsonObject element = jsonObject.getAsJsonObject(setting.getName());
				setting.readJson(element);
			}
		}
	}

	@Override
	public <T extends ModuleSetting<?, ?>> T add(T setting) {
		return settings.add(setting);
	}

	@Override
	public List<ModuleSetting<?, ?>> getSettings() {
		return settings.getSettings();
	}
}
