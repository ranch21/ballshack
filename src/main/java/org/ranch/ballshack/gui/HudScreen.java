package org.ranch.ballshack.gui;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.module.*;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.setting.HudElementData;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.util.DrawUtil;

import java.util.ArrayList;
import java.util.List;

import static org.ranch.ballshack.module.ModuleAnchor.MARGIN;

public class HudScreen extends Screen {

	int dragX = 0;
	int dragY = 0;
	boolean dragging = false;
	ModuleHud draggingModule = null;
	ArrayList<ModuleHud> modules = new ArrayList<>();
	Setting<ArrayList<HudElementData>> hudData = new Setting<>(new ArrayList<>(), "hudElements", new TypeToken<List<HudElementData>>(){}.getType());

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

		context.drawVerticalLine((int)(width * MARGIN), 0, height, Colors.PALLETE_1.hashCode());
		context.drawVerticalLine((int)(width - (width * MARGIN)), 0, height, Colors.PALLETE_1.hashCode());

		context.drawHorizontalLine(0, (int)(width * MARGIN), height / 2, Colors.PALLETE_1.hashCode());
		context.drawHorizontalLine((int)(width - (width * MARGIN)), width, height / 2, Colors.PALLETE_1.hashCode());

		context.drawHorizontalLine((int)(width * MARGIN), (int)(width - (width * MARGIN)), (int)(height * MARGIN), Colors.PALLETE_1.hashCode());
		context.drawHorizontalLine((int)(width * MARGIN), (int)(width - (width * MARGIN)), (int)(height - (height * MARGIN)), Colors.PALLETE_1.hashCode());

		for (ModuleHud module : modules) {
			context.fill(module.X(), module.Y(), module.X() + module.getWidth(), module.Y() + module.getHeight(), Colors.CLICKGUI_2.hashCode());
			DrawUtil.drawPoint(context, module.X()-1, module.Y(), Colors.BORDER);
			DrawUtil.drawPoint(context, module.X()+1, module.Y(), Colors.BORDER);
			DrawUtil.drawPoint(context, module.X(), module.Y()-1, Colors.BORDER);
			DrawUtil.drawPoint(context, module.X(), module.Y()+1, Colors.BORDER);
			DrawUtil.drawLine(context, module.getAnchorPoint().getX(width), module.getAnchorPoint().getY(height), module.X(), module.Y(), Colors.PALLETE_1);

		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			for (ModuleHud module : modules) {

				if (GuiUtil.mouseOverlap(mouseX, mouseY, module.X(), module.Y(), module.getWidth(), module.getHeight())) {
					dragging = true;
					draggingModule = module;
					dragX = (int) mouseX - module.X();
					dragY = (int) mouseY - module.Y();
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
			int x = mouseX - dragX;
			int y = mouseY - dragY;
			draggingModule.setPos(x, y);
			//draggingModule.offsetx = x;
			//draggingModule.offsety = mouseY - dragY;
			int w = width;
			int h = height;
			float m = MARGIN;
			// sybau tsy cd is nt rdbl!!! tf umn 1 ltr vrl nm?
			if (x < w * m) {
				if (y > h / 2) {
					draggingModule.setAnchorPoint(ModuleAnchor.BOTTOM_LEFT);
				} else {
					draggingModule.setAnchorPoint(ModuleAnchor.TOP_LEFT);
				}
			} else if (x > w - (w * m)) {
				if (y > h / 2) {
					draggingModule.setAnchorPoint(ModuleAnchor.BOTTOM_RIGHT);
				} else {
					draggingModule.setAnchorPoint(ModuleAnchor.TOP_RIGHT);
				}
			} else if (y > h * m && y < h - (h * m)) {
				draggingModule.setAnchorPoint(ModuleAnchor.CENTER);
			} else {
				if (y > h / 2) {
					draggingModule.setAnchorPoint(ModuleAnchor.BOTTOM_CENTER);
				} else {
					draggingModule.setAnchorPoint(ModuleAnchor.TOP_CENTER);
				}
			}
		}

		for (ModuleHud module : modules) {
			module.setPos(Math.max(module.X(), 0), Math.max(module.Y(), 0));
			module.setPos(Math.min(module.X(), width), Math.min(module.Y(), height));

			module.setPos(Math.min(module.X(), width - module.getWidth()), Math.min(module.Y(), height - module.getHeight()));
			module.setPos(Math.max(module.X(), 0), Math.max(module.Y(), 0));

			//module.X() = Math.min(module.x, width);

			//module.y = Math.max(module.y, 0);
			//module.y = Math.min(module.y, height);

			//module.x = Math.min(module.x, width - module.getWidth());
			//module.x = Math.max(module.x, 0);

			//module.y = Math.min(module.y, height - module.getHeight());
			//module.y = Math.max(module.y, 0);
		}
	}
}

