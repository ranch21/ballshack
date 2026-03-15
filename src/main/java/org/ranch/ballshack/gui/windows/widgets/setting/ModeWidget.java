package org.ranch.ballshack.gui.windows.widgets.setting;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.setting.module.settings.ModeSetting;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.DrawUtil;

public class ModeWidget<E extends Enum<?>> extends SettingWidget<E> {

	public static final int h = 10;

	private Dropdown dropdown;

	public ModeWidget(String title, int x, int y, int width, int height, ModeSetting<E> setting) {
		super(title, x, y, width, height, setting);
		addFlags(INDENTED);
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY, float delta) {
		if (dropdown != null && dropdown.getRemovalReason() != null) dropdown = null;
		super.render(context, mouseX, mouseY, delta);

		if (dropdown == null) {
			text(setting.getFormattedValue(), 2, (h - mc.textRenderer.fontHeight) / 2 + 1, 0xFFFFFFFF, true);
			int x = getX() + getWidth() - 10;
			int y = getY() + 2;
			DrawUtil.drawLine(
					context,
					x + 1, y + 1, x + 4, y + 4,
					Colors.BORDER_BOTTOM.getColor()
			);
			DrawUtil.drawLine(
					context,
					x + 7, y + 1, x + 4, y + 4,
					Colors.BORDER_BOTTOM.getColor()
			);
		}
	}

	@Override
	public void onPress(Widget widget, Click click) {
		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (dropdown != null) {
				dropdown.remove(RemovalReason.KILLED);
				dropdown = null;
			}

			dropdown = new Dropdown("Select",
					getX(), getY(), getWidth(),
					getHeight() * ((ModeSetting<E>) setting).getEnumValues().length, (ModeSetting<E>) setting
			);

			getRoot().addChild(dropdown);
		}
	}

	public class Dropdown extends SettingWidget<E> {
		public Dropdown(String title, int x, int y, int width, int height, ModeSetting<E> setting) {
			super(title, x, y, width, height, setting);
			addFlags(NO_FILL);
			removeFlags(NO_BORDER);
		}

		@Override
		public void init() {
			super.init();
		}

		@Override
		public void onPress(Widget widget, Click click) {
			int i = 0;
			for (E mode : ((ModeSetting<E>) setting).getEnumValues()) {
				if (GuiUtil.mouseOverlap(click.x(), click.y(), getX(), getY() + i++ * h, getWidth(), h)) {
					setting.setValue(mode);
				}
			}
			remove(RemovalReason.CLOSED);
		}

		@Override
		public void render(DrawContext context, double mouseX, double mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			int i = 0;
			for (E mode : ((ModeSetting<E>) setting).getEnumValues()) {

				int col = BallColor.of(Colors.FILL.getColor()).hashCode();
				if (GuiUtil.mouseOverlap(mouseX, mouseY, getX(), getY() + i * h, getWidth(), h))
					col = Colors.SELECTABLE.getColor().darker().hashCode();
				if (setting.getValue() == mode) col = Colors.SELECTABLE.getColor().hashCode();

				fill(0, i * h, getWidth(), i * h + h, col);
				text(mode.name(), 2, i++ * 10 + (h - mc.textRenderer.fontHeight) / 2 + 1, 0xFFFFFFFF, true);
			}
		}

		@Override
		protected void drawBackground(DrawContext context, double mouseX, double mouseY, float delta) {
			//super.drawBackground(context, mouseX, mouseY);
		}

		@Override
		public boolean alwaysOnTop() {
			return true;
		}
	}
}
