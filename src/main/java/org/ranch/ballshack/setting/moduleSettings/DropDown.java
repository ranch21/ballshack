package org.ranch.ballshack.setting.moduleSettings;

import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

import java.util.List;

public class DropDown extends ModuleSetting<Boolean> {

	private final List<ModuleSetting<?>> settings;
	private int addedHeight = 0;

	public DropDown(String label, List<ModuleSetting<?>> settings) {
		super(label, false);
		this.settings = settings;
	}

	@Override
	public int render(int mouseX, int mouseY) {

		addedHeight = 0;

		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				addedHeight += setting.render(context, x + 2, y + height + addedHeight, width - 2, height, mouseX, mouseY);
			}

			int bY = y + height;

			context.drawVerticalLine(x, bY - 1, bY + addedHeight, Colors.CLICKGUI_3.hashCode()); // indent thinger
			context.drawVerticalLine(x + 1, bY - 1, bY + addedHeight, Colors.BORDER.hashCode());
		}

		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

		/* setting name and arrow */
		drawText(context, this.getName());
		drawTextRightAligned(context, getFormattedValue());

		return height + addedHeight;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				setting.mouseClicked(mouseX, mouseY, button);
			}
		}

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			this.setValue(!this.getValue());
			return true;

		} else if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height + addedHeight) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return true;
		}
		return false;
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				setting.mouseReleased(mouseX, mouseY, button);
			}
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				setting.keyPressed(keyCode, scanCode, modifiers);
			}
		}
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				if (setting.charTyped(chr, modifiers)) {
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

	public List<ModuleSetting<?>> getSettings() {
		return settings;
	}

	public ModuleSetting<?> getSetting(int index) {
		return settings.get(index);
	}
}
