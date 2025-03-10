package org.ranch.ballshack.setting.settings;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

import java.util.List;

public class DropDown extends ModuleSetting<Boolean> {

	private List<ModuleSetting<?>> settings;

	public DropDown(String label, List<ModuleSetting<?>> settings) {
		super(label, false);
		this.settings = settings;
	}

	@Override
	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		int addedHeight = 0;

		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				addedHeight += setting.render(context, x + 2, y + height + addedHeight, width - 2, height, mouseX, mouseY);
			}

			int bY = y + height;

			context.drawVerticalLine(x, bY - 1, bY + addedHeight, Colors.CLICKGUI_3.hashCode()); // indent thinger
			context.drawVerticalLine(x + 1, bY - 1, bY + addedHeight, Colors.BORDER.hashCode());
		}

		context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.hashCode());

		/* setting name and arrow */
		drawText(context, this.getName());
		drawTextRightAligned(context, getFormattedValue());

		return height + addedHeight;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.getValue()) {
			for (ModuleSetting<?> setting : settings) {
				if (setting.mouseClicked(mouseX, mouseY, button)) {
					return true;
				}
			}
		}

		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			this.setValue(!this.getValue());
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
