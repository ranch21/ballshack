package org.ranch.ballshack.setting.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;

public class SettingToggle extends ModuleSetting<Boolean> {
	public SettingToggle(boolean startingValue, String name) {
		super(name, startingValue);
	}

	@Override
	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		if (this.getValue()) {
			context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.brighter().hashCode());
		} else {
			context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.hashCode());
		}

		/* setting name and value */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		context.drawText(textRend, Text.literal(this.getName()),x + 2,y + textInset,0xFFFFFFFF,true);
		context.drawText(textRend, Text.literal(this.getValue() ? "#" : " "), x + width - 8,y + textInset,0xFFFFFFFF,true);

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
}
