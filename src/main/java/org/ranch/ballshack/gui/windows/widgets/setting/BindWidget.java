package org.ranch.ballshack.gui.windows.widgets.setting;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.setting.settings.BindSetting;
import org.ranch.ballshack.setting.settings.ModeSetting;

public class BindWidget extends SettingWidget<Integer> {

	public BindWidget(String title, int x, int y, int width, int height, BindSetting setting) {
		super(title, x, y, width, height, setting);
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);
		text(setting.getFormattedValue(), 2, (getHeight() - mc.textRenderer.fontHeight) / 2 + 1, 0xFFFFFFFF, true);
	}

	@Override
	public void onPress(Widget widget, Click click) {

	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (isFocused()) {
			if (input.getKeycode() == GLFW.GLFW_KEY_BACKSPACE) {
				setting.setValue(BindSetting.NONE);
				setFocused(false);
			} else if (input.getKeycode() != GLFW.GLFW_KEY_ESCAPE) {
				setting.setValue(input.getKeycode());
				setFocused(false);
			}
		}
		return super.keyPressed(input);
	}
}
