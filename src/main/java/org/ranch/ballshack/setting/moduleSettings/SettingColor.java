package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

/**
 * HSV-based color setting that preserves hue at grayscale/dark extremes and avoids precision loss.
 * UI layout:
 * - Header row (like other settings) with label and hex value.
 * - Below: SV square (left-right = S [0..1], top-bottom = V [1..0]) for current hue.
 * - Right of the SV square: vertical hue slider [0..1].
 */
public class SettingColor extends ModuleSetting<Color> {

	// Persistent HSV state to avoid losing hue when S or V are 0
	private double hue;        // [0,1]
	private double saturation; // [0,1]
	private double value;      // [0,1]
	private double alpha;      // [0,1]

	private boolean opened = false;

	// UI interaction flags
	private boolean draggingSV = false;
	private boolean draggingHue = false;
	private boolean draggingAlpha = false;

	// Cached layout for picker area (computed in render)
	private int svX, svY, svW, svH;
	private int hueX, hueY, hueW, hueH;
	private int alphaX, alphaY, alphaW, alphaH;

	public SettingColor(String name, Color startingValue) {
		super(name, startingValue);
		// initialize HSV from starting RGB
		float[] hsb = Color.RGBtoHSB(startingValue.getRed(), startingValue.getGreen(), startingValue.getBlue(), null);
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.value = hsb[2];
	}

	@Override
	public int render(int mouseX, int mouseY) {
		// header background
		context.fill(x, y, x + width, y + height, Colors.CLICKGUI_3.hashCode());

		// header text
		String hex = String.format("#%02X%02X%02X", getValue().getRed(), getValue().getGreen(), getValue().getBlue());
		drawText(context, this.getName() + ": ");
		if (opened) {
			drawTextRightAligned(context, hex);
		} else {
			drawTextRightAligned(context, "+");
		}

		if (!opened) return height;

		// Layout for picker area
		int pad = 2;
		int pickerTop = y + height; // draw below header
		// Make SV square as large as possible while reserving hue slider width
		int hueSliderWidth = 6;
		int svSize = 60;

		// SV square bounds
		svX = x + pad;
		svY = pickerTop + pad;
		svW = svSize;
		svH = svSize;

		// Hue slider bounds (vertical on the right)
		hueX = x + width - hueSliderWidth - 2;
		hueY = svY;
		hueW = hueSliderWidth;
		hueH = svH;

		alphaX = hueX - hueSliderWidth - 3;
		alphaY = svY;
		alphaW = hueSliderWidth;
		alphaH = svH;

		context.fill(x, y + height, x + width, y + height + pad + svH + pad, Colors.CLICKGUI_3.hashCode());

		// While dragging, update HSV from current mouse position
		if (draggingSV) {
			updateSVFromMouse(mouseX, mouseY);
		}
		if (draggingHue) {
			updateHueFromMouse(mouseX, mouseY);
		}
		if (draggingAlpha) {
			updateAlphaFromMouse(mouseX, mouseY);
		}

		// Draw SV square for current hue using per-pixel HSV to avoid alpha blending issues
		for (int ix = 0; ix < svW; ix++) {
			double s = svW <= 1 ? 0 : ix / (double) (svW - 1);
			for (int iy = 0; iy < svH; iy++) {
				double v = svH <= 1 ? 1 : 1.0 - iy / (double) (svH - 1);
				int rgb = Color.HSBtoRGB((float) hue, (float) s, (float) v);
				DrawUtil.drawPoint(context, svX + ix, svY + iy, new Color(rgb));
			}
		}
		DrawUtil.drawOutline(context, svX, svY, svW, svH);

		// Draw hue slider as vertical rainbow
		int steps = Math.max(6, svH); // smooth enough
		for (int i = 0; i < steps; i++) {
			float t = i / (float) (steps - 1);
			Color c = Color.getHSBColor(t, 1f, 1f);
			context.fill(hueX, hueY + i * hueH / steps, hueX + hueW, hueY + (i + 1) * hueH / steps, c.hashCode());
		}
		DrawUtil.drawOutline(context, hueX, hueY, hueW, hueH);

		for (int i = 0; i < steps; i++) {
			float t = i / (float) (steps - 1);
			Color c = Color.getHSBColor(0f, 0f, t);
			context.fill(alphaX, alphaY + i * alphaH / steps, alphaX + alphaW, alphaY + (i + 1) * alphaH / steps, c.hashCode());
		}
		DrawUtil.drawOutline(context, alphaX, alphaY, alphaW, alphaH);

		// Draw cursors
		int svCursorX = svX + (int) Math.round(saturation * (svW - 1));
		int svCursorY = svY + (int) Math.round((1.0 - value) * (svH - 1));
		Color cursorColor = getValue().getRed() + getValue().getGreen() + getValue().getBlue() > 380 ? Color.BLACK : Color.WHITE;
		context.enableScissor(svX, svY, svW + svX, svH + svY);
		DrawUtil.drawOutline(context, svCursorX - 1, svCursorY - 1, 3, 3, cursorColor);
		context.disableScissor();


		int hueCursorY = hueY + (int) Math.round(hue * (hueH - 1));
		context.drawHorizontalLine(hueX - 1, hueX + hueW, hueCursorY, Color.WHITE.hashCode());

		int alphaCursorY = alphaY + (int) Math.round((-alpha + 1) * (alphaH - 1));
		context.drawHorizontalLine(alphaX - 1, alphaX + alphaW, alphaCursorY, Color.WHITE.hashCode());

		// Update displayed Color from HSV without re-deriving hue from RGB
		updateColorFromHSV();

		// total used height
		return height + pad + svH + pad;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && opened) {
			if (GuiUtil.mouseOverlap(mouseX, mouseY, svX, svY, svW, svH)) {
				draggingSV = true;
				updateSVFromMouse(mouseX, mouseY);
				return true;
			}
			if (GuiUtil.mouseOverlap(mouseX, mouseY, alphaX, alphaY, alphaW, alphaH)) {
				draggingAlpha = true;
				updateAlphaFromMouse(mouseX, mouseY);
				return true;
			}
			if (GuiUtil.mouseOverlap(mouseX, mouseY, hueX, hueY, hueW, hueH)) {
				draggingHue = true;
				updateHueFromMouse(mouseX, mouseY);
				return true;
			}
			// also allow clicking header row to consume
			if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height)) {
				return true;
			}
		}
		if (GuiUtil.mouseOverlap(mouseX, mouseY, x, y, width, height) && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			opened = !opened;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			draggingSV = false;
			draggingHue = false;
			draggingAlpha = false;
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		// no-op
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		return false;
	}

	private void updateSVFromMouse(double mouseX, double mouseY) {
		double s = (mouseX - svX) / (double) (svW - 1);
		double v = 1.0 - (mouseY - svY) / (double) (svH - 1);
		saturation = clamp01(s);
		value = clamp01(v);
		updateColorFromHSV();
	}

	private void updateHueFromMouse(double mouseX, double mouseY) {
		double h = (mouseY - hueY) / (double) (hueH - 1);
		hue = clamp01(h);
		updateColorFromHSV();
	}

	private void updateAlphaFromMouse(double mouseX, double mouseY) {
		double a = (mouseY - alphaY) / (double) (alphaH - 1);
		alpha = clamp01(-a + 1);
		updateColorFromHSV();
	}

	protected void updateColorFromHSV() {
		// Only convert in this direction; do not recompute hue from RGB
		int rgb = Color.HSBtoRGB((float) hue, (float) saturation, (float) value);
		rgb = (rgb & 0x00FFFFFF) | ((int)(alpha * 255) << 24);
		Color col = new Color(rgb, true);
		setValue(col);
		Colors.refreshFromSettings();
	}

	private static double clamp01(double d) {
		if (d < 0) return 0;
		if (d > 1) return 1;
		return d;
	}

	@Override
	public String getFormattedValue() {
		Color c = getValue();
		return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}

	@Override
	public JsonObject getJson() {
		Color c = getValue();
		JsonObject obj = new JsonObject();
		obj.addProperty("r", c.getRed());
		obj.addProperty("g", c.getGreen());
		obj.addProperty("b", c.getBlue());
		obj.addProperty("ai", c.getAlpha());
		// Also persist HSV to preserve hue across gray loads (optional, for future)
		obj.addProperty("h", hue);
		obj.addProperty("s", saturation);
		obj.addProperty("v", value);
		obj.addProperty("af", alpha);
		return obj;
	}

	@Override
	public void readJson(JsonObject json) {
		// Prefer HSV if present to preserve hue on grayscale
		if (json.has("h") && json.has("s") && json.has("v")) {
			this.hue = clamp01(json.get("h").getAsDouble());
			this.saturation = clamp01(json.get("s").getAsDouble());
			this.value = clamp01(json.get("v").getAsDouble());
			this.value = clamp01(json.get("af").getAsDouble());
			updateColorFromHSV();
			return;
		}
		// Fallback to RGB
		if (json.has("r") && json.has("g") && json.has("b")) {
			int r = json.get("r").getAsInt();
			int g = json.get("g").getAsInt();
			int b = json.get("b").getAsInt();
			int a = json.get("ai").getAsInt();
			Color c = new Color(r, g, b, a);
			setValue(c);
			float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			this.hue = hsb[0];
			this.saturation = hsb[1];
			this.value = hsb[2];
			return;
		}
		// Unknown format: ignore
	}

	// Handle dragging while mouse is held by checking continuously in render isn't practical here.
	// Rely on the GUI framework to call mouseClicked and mouseReleased; a drag move should be handled by re-calling mouseClicked logic.
}
