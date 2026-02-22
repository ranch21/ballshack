package org.ranch.ballshack.gui.windows.clickgui;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.AutoFitWindow;
import org.ranch.ballshack.gui.windows.widgets.TextWidget;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettingsGroup;

public class ModuleSettingWindow extends AutoFitWindow {

	private final ModuleSettingsGroup settings;

	public ModuleSettingWindow(ModuleSettingsGroup settings, int x, int y, int width, int height) {
		super(settings.name, x, y, width, height, false, true, 5, 5);
		this.settings = settings;
	}

	@Override
	public void init() {
		super.init();

		for (ModuleSetting<?, ?> setting : settings.getSettings()) {
			addWidget(setting);
		}
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		fit();
		Window prev = null;
		for (Window widget : getChildren()) {
			int py = 5;
			if (prev != null) {
				py = prev.getY() - prev.getInsideOffsetY() - getY() + prev.getHeight() + 5;
			}

			widget.setY(py);
			if (!(widget instanceof TextWidget))
				prev = widget;
		}
		super.render(context, mouseX, mouseY);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		mc.setScreen(getRootScreen().parent);
	}

	private void addWidget(ModuleSetting<?, ?> setting) {
		addChild(new TextWidget(
				setting.getName(),
				2, 0,
				10, 10
		));

		addChild(setting.getWidget(
				mc.textRenderer.getWidth(setting.getName()) + 4,
				0,
				getWidth() - mc.textRenderer.getWidth(setting.getName()) - 8, 10
		));
	}
}
