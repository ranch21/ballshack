package org.ranch.ballshack.setting.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
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
				addedHeight += setting.render(context, x, y + height + addedHeight, width, height, mouseX, mouseY);
			}
		}

		context.fill(x, y, x+width, y+height, Colors.CLICKGUI_3.hashCode());

		/* setting name and arrow */
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (height - textRend.fontHeight) / 2;
		context.drawText(textRend, Text.literal(this.getName()),x + 2,y + 2,0xFFFFFFFF,true);
		context.drawText(textRend, Text.literal(this.getValue() ? "-" : "+"),x + width - 8,y + textInset, 0xFFFFFFFF,true);

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

	public List<ModuleSetting<?>> getSettings() {
		return settings;
	}

	public ModuleSetting<?> getSetting(int index) {
		return settings.get(index);
	}
}
