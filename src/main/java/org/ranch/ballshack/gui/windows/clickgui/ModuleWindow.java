package org.ranch.ballshack.gui.windows.clickgui;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.windows.widgets.ButtonWidget;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;
import org.ranch.ballshack.util.rendering.DrawUtil;

public class ModuleWindow extends ButtonWidget {

	private final Module module;

	public ModuleWindow(Module module, int x, int y, int width, int height) {
		super(module.getName(), x, y, width, height);
		setCallback((button, click) -> {
			if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				module.toggle();
			} else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				ModuleSettingScreen screen = new ModuleSettingScreen(module, getRootScreen());
				screen.setScale(ModuleManager.getModuleByClass(ClickGui.class).scale.getValueFloat());
				BallsHack.mc.setScreen(screen);
			}
		});
		this.module = module;
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		super.render(context, mouseX, mouseY);
		if (overlaps(mouseX, mouseY)) {
			DrawUtil.queueTooltip(getX() + getWidth(), getY(), module.getTooltip());
		}
	}

	@Override
	protected void drawBackground(DrawContext context, double mouseX, double mouseY) {
		if (overlaps(mouseX, mouseY)) {
			fill(0, 0, getWidth(), getHeight(), Colors.SELECTABLE.getColor().hashCode());
		} else if (module.isEnabled()) {
			fill(0, 0, getWidth(), getHeight(), Colors.SELECTABLE.getColor().hashCode());
		} else {
			fill(0, 0, getWidth(), getHeight(), Colors.FILL.getColor().hashCode());
		}
	}
}
