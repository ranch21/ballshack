package org.ranch.ballshack.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.module.ModuleManager;

import java.util.ArrayList;

public class HudScreen extends Screen {

	int dragX = 0;
	int dragY = 0;
	boolean dragging = false;
	ModuleHud draggingModule = null;
	ArrayList<ModuleHud> modules = new ArrayList<>();

	public HudScreen() {
		super(NarratorManager.EMPTY);
	}

	@Override
	protected void init() {
		modules.clear();
		for (Module module : ModuleManager.getModulesByCategory(ModuleCategory.HUD)) {
			if (module instanceof ModuleHud moduleHud) {
				modules.add(moduleHud);
			}
		}
		super.init();
	}

	@Override
	public void tick() {
		super.tick();
		MinecraftClient mc = MinecraftClient.getInstance();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {

		//super.render(context, mouseX, mouseY, delta);

		context.fill(0, 0, width, height, Colors.CLICKGUI_BACKGROUND.hashCode());

		/*TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		DrawUtil.drawText(context,textRend, BallsHack.title.getValue(), 5, 5,Colors.PALLETE_1,true);
		DrawUtil.drawText(context,textRend, BallsHack.version, 5 + textRend.getWidth(BallsHack.title.getValue() + " "), 5, Color.WHITE,true);
		*/
		handleDrag(mouseX, mouseY);


		for (ModuleHud module : modules) {
			context.fill(module.x, module.y, module.x + module.getWidth(), module.y + module.getHeight(), Colors.CLICKGUI_2.hashCode());
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			for (ModuleHud module : modules) {

				if (GuiUtil.mouseOverlap(mouseX, mouseY, module.x, module.y, module.getWidth(), module.getHeight())) {
					dragging = true;
					draggingModule = module;
					dragX = (int) mouseX - module.x;
					dragY = (int) mouseY - module.y;
					return true;
				}
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) dragging = false;

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {

		return super.charTyped(chr, modifiers);
	}

	public boolean shouldPause() {
		return false;
	}

	protected void handleDrag(int mouseX, int mouseY) {
		if (dragging) {
			draggingModule.x = mouseX - dragX;
			draggingModule.y = mouseY - dragY;
		}

		for (ModuleHud module : modules) {
			module.x = Math.max(module.x, 0);
			module.x = Math.min(module.x, width);

			module.y = Math.max(module.y, 0);
			module.y = Math.min(module.y, height);

			module.x = Math.min(module.x, width - module.getWidth());
			module.x = Math.max(module.x, 0);

			module.y = Math.min(module.y, height - module.getHeight());
			module.y = Math.max(module.y, 0);
		}
	}
}

