package org.ranch.ballshack.gui.windows.widgets.setting;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.setting.ISetting;

public class CheckboxWidget extends SettingWidget<Boolean> {

	public CheckboxWidget(String title, int x, int y, int width, int height, ISetting<Boolean> setting) {
		super(title, x, y, width, height, setting);
		addFlags(INDENTED);
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		if (setting.getValue())
			fill(1, 1, getWidth() - 1, getHeight() - 1, Colors.SELECTED.getColor().hashCode());
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		//if (inside((int) click.x(), (int) click.y())) {
		setting.setValue(!setting.getValue());
		return true;
		//}
		//return false;
	}
}
