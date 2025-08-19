package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.util.TextUtil;

public class SettingSlider extends ModuleSetting<Double> {

	private final double min;
	private final double max;
	private final double step;

	private boolean holding = false;

	public SettingSlider(double startingValue, String name, double min, double max, double step) {
		super(name, startingValue);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	@Override
	public int render(int mouseX, int mouseY) {

		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

		if (holding) {
			double mousePercent = (mouseX - x) / (double) width;
			this.setValue(roundToStep(Math.max(Math.min(mousePercent * (max - min) + min, max), min)));
		}

		double valuePercent = (this.getValue() - min) / (max - min);

		int notchPos = (int) (valuePercent * (width));

		context.fill(x, y, x + notchPos, y + height, Colors.SELECTABLE.hashCode());

		/* setting name and value */
		drawText(context, this.getName() + ": " + getFormattedValue());
		//drawText(context, TextUtil.formatDecimal(this.getValue()), true);

		return height;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			holding = true;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		holding = false;
	}

	@Override
	public String getFormattedValue() {
		return TextUtil.formatDecimal(this.getValue());
	}

	private double roundToStep(double value) {
		return Math.round(value / step) * step;
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("value", getValue());
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		setValue(jsonObject.get("value").getAsDouble());
	}
}
