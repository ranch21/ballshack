package org.ranch.ballshack.setting.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
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
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		String keyName = GLFW.glfwGetKeyName(this.getValue(), 0);
		if (keyName == null) {
			drawValue(context);
		} else if (this.getValue() == 0) {
			context.drawText(textRend, Text.literal(this.getName() + ": " + "None"),x + 2,y + 2,0xFFFFFFFF,true);
		} else {
			context.drawText(textRend, Text.literal(this.getName() + ": " + keyName),x + 2,y + 2,0xFFFFFFFF,true);
		}

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
			this.setValue(keyCode);
			selected = false;
		}
	}
}
