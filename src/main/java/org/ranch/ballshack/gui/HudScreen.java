package org.ranch.ballshack.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.NarratorManager;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.module.*;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ranch.ballshack.module.ModuleAnchor.MARGIN;

public class HudScreen extends Screen {

	int dragX = 0;
	int dragY = 0;
	boolean dragging = false;
	final int snapRange = 2;
	ModuleHud draggingModule = null;
	final ArrayList<ModuleHud> modules = new ArrayList<>();

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
		if (dragging) {
			List<Snap> snaps = handleDrag(mouseX, mouseY);
			if (snaps != null) {
				if (snaps.get(0) != null) {
					snaps.get(0).drawSnap(context, Color.WHITE);
				}
				if (snaps.get(1) != null) {
					snaps.get(1).drawSnap(context, Color.WHITE);
				}
			}
		}
		clampPositions();

		drawAnchorRegions(context, Color.DARK_GRAY);

		for (ModuleHud module : modules) {

			if (!module.isEnabled()) continue;
			DrawUtil.drawOutline(context, module.X() - module.xOffset(), module.Y() - module.yOffset(), module.getWidth(), module.getHeight(), Color.WHITE, Color.WHITE);
			//context.fill(module.X() - module.xOffset(), module.Y() - module.yOffset(), module.X() - module.xOffset() + module.getWidth(), module.Y() - module.yOffset() + module.getHeight(), Colors.CLICKGUI_1.getColor().hashCode());
			DrawUtil.drawPoint(context, module.X() - 1, module.Y(), Color.WHITE);
			DrawUtil.drawPoint(context, module.X() + 1, module.Y(), Color.WHITE);
			DrawUtil.drawPoint(context, module.X(), module.Y() - 1, Color.WHITE);
			DrawUtil.drawPoint(context, module.X(), module.Y() + 1, Color.WHITE);
			DrawUtil.drawLine(context, module.getAnchorPoint().getX(width), module.getAnchorPoint().getY(height), module.X(), module.Y(), Color.GRAY);
		}
	}

	private void drawAnchorRegions(DrawContext context, Color color) {
		context.drawVerticalLine((int) (width * MARGIN), 0, height, color.hashCode());
		context.drawVerticalLine((int) (width - (width * MARGIN)), 0, height, color.hashCode());

		context.drawHorizontalLine(0, (int) (width * MARGIN), height / 2, color.hashCode());
		context.drawHorizontalLine((int) (width - (width * MARGIN)), width, height / 2, color.hashCode());

		context.drawHorizontalLine((int) (width * MARGIN), (int) (width - (width * MARGIN)), (int) (height * MARGIN), color.hashCode());
		context.drawHorizontalLine((int) (width * MARGIN), (int) (width - (width * MARGIN)), (int) (height - (height * MARGIN)), color.hashCode());
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {

		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			for (ModuleHud module : modules) {

				if (!module.isEnabled()) continue;

				if (GuiUtil.mouseOverlap(click.x(), click.y(), module.X() - module.xOffset(), module.Y() - module.yOffset(), module.getWidth(), module.getHeight())) {
					dragging = true;
					draggingModule = module;
					dragX = (int) click.x() - module.X();
					dragY = (int) click.y() - module.Y();
					return true;
				}
			}
		}

		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean mouseReleased(Click click) {

		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) dragging = false;

		return super.mouseReleased(click);
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {

		return super.keyPressed(keyInput);
	}

	@Override
	public boolean charTyped(CharInput charInput) {

		return super.charTyped(charInput);
	}

	public boolean shouldPause() {
		return false;
	}

	private List<Snap> handleDrag(int mouseX, int mouseY) {
		int x = mouseX - dragX;
		int y = mouseY - dragY;
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
		draggingModule.setPos(x, y);
		List<Snap> snaps = getSnaps(draggingModule);
		if (snaps != null) {
			if (snaps.get(0) != null) {
				snaps.get(0).setModule(draggingModule);
			}
			if (snaps.get(1) != null) {
				snaps.get(1).setModule(draggingModule);
			}
		}
		return snaps;

	}

	private void clampPositions() {
		for (ModuleHud module : modules) {
			module.setPos(Math.min(module.X(), width - module.getWidth() + module.xOffset()), Math.min(module.Y(), height - module.getHeight() + module.yOffset()));
			module.setPos(Math.max(module.X(), module.xOffset()), Math.max(module.Y(), module.yOffset()));
		}
	}

	private List<Snap> getSnaps(ModuleHud module) {
		for (ModuleHud moduleHud : modules) {
			if (!moduleHud.isEnabled()) continue;
			if (moduleHud == module) continue;
			int x = module.X() - module.xOffset();
			int y = module.Y() - module.yOffset();
			int w = module.getWidth();
			int h = module.getHeight();
			int sx = moduleHud.X() - moduleHud.xOffset();
			int sy = moduleHud.Y() - moduleHud.yOffset();
			int sw = moduleHud.getWidth();
			int sh = moduleHud.getHeight();

			Snap xSnap = getSnap(x, w, sx, sw, Axis.X);
			Snap ySnap = getSnap(y, h, sy, sh, Axis.Y);

			if (xSnap != null || ySnap != null) return Arrays.asList(xSnap, ySnap);
		}
		return null;
	}

	private Snap getSnap(int p, int pl, int sp, int spl, Axis axis) {

		if (Math.abs(p - sp) <= snapRange) {
			return new Snap(sp, 0, axis);
		} else if (Math.abs((p + pl) - (sp + spl)) <= snapRange) {
			return new Snap(spl + sp, pl, axis);
		} else if (Math.abs(p + pl - sp) <= snapRange) {
			return new Snap(sp, pl, axis);
		} else if (Math.abs(p - (sp + spl)) <= snapRange) {
			return new Snap(spl + sp, 0, axis);
		}
		return null;
	}

	private class Snap {
		final int p;
		final int across;
		final Axis axis;

		public Snap(int p, int across, Axis axis) {
			this.p = p;
			this.axis = axis;
			this.across = across;
		}

		public void setModule(ModuleHud module) {
			if (axis == Axis.X) {
				module.setPos(p - across + module.xOffset(), module.Y());
			} else if (axis == Axis.Y) {
				module.setPos(module.X(), p - across + module.yOffset());
			}
		}

		public void drawSnap(DrawContext context, Color color) {
			if (axis == Axis.X) {
				context.drawVerticalLine(p, 0, DrawUtil.getScreenHeight(), color.hashCode());
			} else {
				context.drawHorizontalLine(0, DrawUtil.getScreenWidth(), p, color.hashCode());
			}
		}
	}

	private enum Axis {
		X,
		Y
	}

	@Override
	protected void applyBlur(DrawContext context) {

	}
}

