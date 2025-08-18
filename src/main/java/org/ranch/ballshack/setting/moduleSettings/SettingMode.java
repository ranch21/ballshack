package org.ranch.ballshack.setting.moduleSettings;

import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

import java.util.List;

public class SettingMode extends ModuleSetting<Integer> {

	private final List<String> modes;

	public SettingMode(int value, String name, List<String> modes) {
		super(name, value);
		this.modes = modes;
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
			int i = this.getValue();
			i++;
			if (i >= this.modes.size()) {
				i = 0;
			}

			this.setValue(i);
			return true;
		}
		return false;
	}

	@Override
	public String getFormattedValue() {
		return this.modes.get(this.getValue());
	}
}
