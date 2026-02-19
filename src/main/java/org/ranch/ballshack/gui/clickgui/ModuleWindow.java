package org.ranch.ballshack.gui.clickgui;

import net.minecraft.client.gui.Click;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.ButtonWidget;
import org.ranch.ballshack.module.Module;

public class ModuleWindow extends ButtonWidget {

	Module module;

	public ModuleWindow(Module module, int x, int y, int width, int height) {
		super(module.getName(), x, y, width, height, (button) -> {
			button.getRoot().addChild(
					new Window("Settings", 200, 200, 50, 50)
			);
		});
		this.module = module;
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		return super.mouseClicked(click, doubled);
	}
}
