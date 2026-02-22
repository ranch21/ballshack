package org.ranch.ballshack.gui.windows.widgets.setting;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.setting.ISetting;

import java.awt.*;

public class SliderWidget extends SettingWidget<Double> {

	private final double min, max, step;
	private boolean holding;

	public SliderWidget(String title, int x, int y, int width, int height, ISetting<Double> setting, double min, double max, double step) {
		super(title, x, y, width, height, setting);
		this.min = min;
		this.max = max;
		this.step = step;
		addFlags(NO_FILL | INDENTED);
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);

		fill(0, 0, getWidth(), getHeight(), Colors.FILL_DARK.getColor().hashCode());

		if (holding) {
			double mousePercent = (mouseX - getX()) / (double) getWidth();
			setting.setValue(roundToStep(Math.max(Math.min(mousePercent * (max - min) + min, max), min)));
		}

		double valuePercent = (setting.getValue() - min) / (max - min);

		int notchPos = (int) (valuePercent * (getWidth()));

		fill(0, 0, notchPos, getHeight(), Colors.SELECTED.getColor().hashCode());

		if (step != 0) {
			double notchStep = step;

			for (int i = 0; !(!(i < 10) || !((max - min) / notchStep > (double) getWidth() / 2)); i++) {
				notchStep *= 2;
			}

			int stepCount = (int) Math.round((max - min) / notchStep);

			for (int i = 0; i <= stepCount; i++) {
				double value = min + i * notchStep;
				double percent = (value - min) / (max - min);
				int xPos = (int) (getX() + percent * getWidth());
				if (xPos >= getX() + getWidth() - 1) break;

				context.drawVerticalLine(
						xPos,
						getY() + getHeight() - 4,
						getY() + getHeight(),
						Color.LIGHT_GRAY.hashCode()
				);
			}
		}

		text(setting.getFormattedValue(), 2, (getHeight() - mc.textRenderer.fontHeight) / 2 + 1, 0xFFFFFFFF, true);
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
