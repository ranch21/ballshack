package org.ranch.ballshack.gui.windows;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.WindowData;
import org.ranch.ballshack.gui.balls.Ball;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowScreen extends Screen implements IWindow {

	private static final List<Window> windows = new ArrayList<>();

	public WindowScreen() {
		super(Text.empty());
	}

	@Override
	public void init() {
		super.init();
		windows.clear();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		windows.removeIf(window -> window.getRemovalReason() != null);

		for (Window window : windows) {
			window.render(context, mouseX, mouseY);
		}
		DrawUtil.drawTooltip(context);
		DrawUtil.clearTooltip();
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	public void mouseMoved(double mouseX, double mouseY) {
		windows.forEach(window -> window.mouseMoved(mouseX, mouseY));
	}

	public boolean mouseClicked(Click click, boolean doubled) {
		for (Window window : windows) {
			if (window.mouseClicked(click, doubled)) return true;
		}
		return false;
	}

	public boolean mouseReleased(Click click) {
		windows.forEach(window -> window.mouseReleased(click));
		return false;
	}

	public boolean mouseDragged(Click click, double offsetX, double offsetY) {
		windows.forEach(window -> window.mouseDragged(click, offsetX, offsetY));
		return false;
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		windows.forEach(window -> window.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
		return false;
	}

	public boolean keyPressed(KeyInput input) {
		windows.forEach(window -> window.keyPressed(input));
		return false;
	}

	public boolean keyReleased(KeyInput input) {
		windows.forEach(window -> window.keyReleased(input));
		return false;
	}

	public boolean charTyped(CharInput input) {
		windows.forEach(window -> window.charTyped(input));
		return false;
	}

	@Override
	public void addChild(Window child) {
		windows.add(child);
		child.setParent(this);
	}

	@Override
	public List<Window> getChildren() {
		return windows;
	}

	@Override
	public void setParent(IWindow parent) {

	}

	@Override
	public IWindow getParent() {
		return null;
	}

	@Override
	public IWindow getRoot() {
		return this;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {

	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {

	}

	@Override
	public int getInsideOffsetX() {
		return 0;
	}

	@Override
	public int getInsideOffsetY() {
		return 0;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public void setX(int x) {

	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public void setY(int y) {

	}
}
