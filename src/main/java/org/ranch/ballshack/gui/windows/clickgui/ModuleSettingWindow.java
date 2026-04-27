package org.ranch.ballshack.gui.windows.clickgui;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.AutoFitWindow;
import org.ranch.ballshack.gui.windows.widgets.TextWidget;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.setting.module.ModuleSetting;
import org.ranch.ballshack.setting.module.ModuleSettingsGroup;

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
	public void render(DrawContext context, double mouseX, double mouseY, float delta) {
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
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void remove(RemovalReason reason) {
		mc.setScreen(ClickGuiScreen.getInstance(null));
		//super.remove(reason);
	}

	private void addWidget(ModuleSetting<?, ?> setting) {
		Widget w = setting.getWidget(
				(setting.hasLabel() ? mc.textRenderer.getWidth(setting.getName()) + 4 : 2),
				0,
				getWidth() - (setting.hasLabel() ? mc.textRenderer.getWidth(setting.getName()) + 8 : 4), 10
		);
		if (w == null) return;

		if (setting.hasLabel()) {
			addChild(new TextWidget(
					setting.getName(),
					2, 0,
					10, 10
			));
		}

		addChild(w);
	}
}
