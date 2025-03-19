package org.ranch.ballshack.setting.settings;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

public class SettingBind extends ModuleSetting<Integer> {

	private boolean selected;

	public SettingBind(int startingValue, String name) {
		super(name, startingValue);
	}

	@Override
	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.hashCode());

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
		}  else {
			return keyName;
		}
	}
}
