package org.ranch.ballshack.gui.clickgui;

import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.AutoFitWindow;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;

import java.util.List;

public class CategoryWindow extends AutoFitWindow {

	private final ModuleCategory category;

	public CategoryWindow(ModuleCategory category, int x, int y, int width, int height) {
		super(category.name(), x, y, width, height, true, true, 5, 5);
		this.category = category;
	}

	@Override
	public void init() {
		super.init();
		List<Module> modules = ModuleManager.getModulesByCategory(category);

		int s = 5;
		int h = 15;

		int i = 0;
		for (Module module : modules) {
			addChild(new ModuleWindow(module, 5, i++ * (s + h) + s, 40, h));
		}
	}
}
