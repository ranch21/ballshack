package org.ranch.ballshack.gui.clickgui;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.AutoFitWindow;
import org.ranch.ballshack.gui.windows.widgets.TextWidget;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.settings.BooleanSetting;

import java.util.List;

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
		super.render(context, mouseX, mouseY);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		mc.setScreen(new ClickGuiScreen());
	}

	private void addWidget(ModuleSetting<?, ?> setting) {
		int y = getMaxScrollY() + 5;

		addChild(new TextWidget(
				setting.getName(),
				2, y,
				10, 10
		));

		addChild(setting.getWidget(
				mc.textRenderer.getWidth(setting.getName()) + 4,
				y,
				getWidth() - mc.textRenderer.getWidth(setting.getName()) - 8, 10
		));
	}
}
