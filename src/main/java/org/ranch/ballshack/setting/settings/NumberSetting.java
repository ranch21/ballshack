package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.SliderWidget;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.util.TextUtil;

public class NumberSetting extends ModuleSetting<Double, NumberSetting> {

	private double min = Double.NEGATIVE_INFINITY;
	private double max = Double.POSITIVE_INFINITY;
	private double step = 0;

	public NumberSetting(String name, double value) {
		super(name, value);
	}

	public NumberSetting min(double min) {
		this.min = min;
		return self();
	}

	public NumberSetting max(double max) {
		this.max = max;
		return self();
	}

	public NumberSetting step(double step) {
		this.step = step;
		return self();
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getStep() {
		return step;
	}


	public int getValueInt() {
		return (int) (double) getValue();
	}

	public float getValueFloat() {
		return (float) (double) getValue();
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return new SliderWidget(getName(), x, y, width, height, this, min, max, step);
	}

	@Override
	public String getFormattedValue() {
		return TextUtil.formatDecimal(getValue(), "#.##");
	}

	@Override
	public JsonElement getJson() {
		return new JsonPrimitive(getValue());
	}

	@Override
	public void readJson(JsonElement jsonElement) {
		setValue(jsonElement.getAsDouble());
	}
}
