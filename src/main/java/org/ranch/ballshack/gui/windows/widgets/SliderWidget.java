package org.ranch.ballshack.gui.windows.widgets;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.setting.ISetting;

public class SliderWidget extends SettingWidget<Double> {

	private final double min, max, step;
	private boolean holding;

	public SliderWidget(String title, int x, int y, int width, int height, ISetting<Double> setting, double min, double max, double step) {
		super(title, x, y, width, height, setting);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		fill(0, 0, getWidth(), getHeight(), Colors.CLICKGUI_2.getColor().hashCode());

		if (holding) {
			double mousePercent = (mouseX - getX()) / (double) getWidth();
			setting.setValue(roundToStep(Math.max(Math.min(mousePercent * (max - min) + min, max), min)));
		}

		double valuePercent = (setting.getValue() - min) / (max - min);

		int notchPos = (int) (valuePercent * (getWidth()));

		fill(0, 0, notchPos, getHeight(), Colors.SELECTABLE.getColor().hashCode());

		/* setting name and value */
		text(setting.getFormattedValue(), 0, 0, 0xFFFFFFFF, true);
		//drawText(context, TextUtil.formatDecimal(this.getValue()), true);
	}

	private double roundToStep(double value) {
		if (step == 0) return value;
		return Math.round(value / step) * step;
	}

	@Override
	public void onPress(Widget widget, Click click) {
		holding = true;
	}

	@Override
	public boolean mouseReleased(Click click) {
		super.mouseReleased(click);
		holding = false;
		return false;
	}
}
