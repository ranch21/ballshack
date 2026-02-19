package org.ranch.ballshack.gui.clickgui;

import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;

import java.util.List;

public class CategoryWindow extends Window {

	public CategoryWindow(ModuleCategory category, int x, int y, int width, int height) {
		super(category.name(), x, y, width, height);

		List<Module> modules = ModuleManager.getModulesByCategory(category);
		int i = 0;
		for (Module module : modules) {
			addChild(new ModuleWindow(module, 5, 25 * i++ + 5, 40, 20));
		}
	}
}
