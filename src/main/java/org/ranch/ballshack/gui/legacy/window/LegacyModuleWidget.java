package org.ranch.ballshack.gui.window.legacy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.legacy.LegacyColors;
import org.ranch.ballshack.gui.legacy.LegacyGuiUtil;
import org.ranch.ballshack.module.Module;

public class LegacyModuleWidget {
	
	private Module module;

	private int x;
	private int y;

	private int width;
	private int height;
	
	public LegacyModuleWidget(Module module) {
		this.module = module;
	}

	public int render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;

		if (LegacyGuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
			context.fill(x, y, x + width, y + height, LegacyColors.CLICKGUI_2.darker().hashCode());
		} else {
			context.fill(x, y, x + width, y + height, LegacyColors.CLICKGUI_2.hashCode());
		}

		/* module name */
		context.drawText(textRend, Text.literal(module.getName()),x + 2,y + 2,0xFFFFFFFF,true);

		return height;
	}

	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (LegacyGuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				module.toggle();
			}
		}
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {

	}
	
}
