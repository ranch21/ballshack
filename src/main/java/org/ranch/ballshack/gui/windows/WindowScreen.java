package org.ranch.ballshack.gui.windows;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.joml.Matrix3x2fStack;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.util.ArrayList;
import java.util.List;

public class WindowScreen extends Screen implements IWindow {

	private static final List<Window> windows = new ArrayList<>();
	public Screen parent;
	private float scale = 0.5F;

	public WindowScreen(Screen parent) {
		super(Text.empty());
		this.parent = parent;
	}

	@Override
	public void init() {
		super.init();
		windows.clear();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {

		Matrix3x2fStack stack = context.getMatrices();
		stack.pushMatrix();
		stack.scale(scale);

		windows.removeIf(window -> window.getRemovalReason() != null);

		sortChildren();

		for (int i = windows.size() - 1; i >= 0; i--) {
			windows.get(i).render(context, mouseX / scale, mouseY / scale);
		}
		DrawUtil.drawTooltip(context);
		DrawUtil.clearTooltip();

		stack.popMatrix();
	}

	protected void sortChildren() {
		windows.sort((a, b) -> Boolean.compare(b.isFocused(), a.isFocused()));
		windows.sort((a, b) -> Boolean.compare(b.alwaysOnTop(), a.alwaysOnTop()));
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	private Click scaleClick(Click click) {
		return new Click(click.x() / scale, click.y() / scale, click.buttonInfo());
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	public void mouseMoved(double mouseX, double mouseY) {
		windows.forEach(window -> window.mouseMoved(mouseX / scale, mouseY / scale));
	}

	public boolean mouseClicked(Click click, boolean doubled) {
		for (Window window : windows) {
			if (window.mouseClicked(scaleClick(click), doubled)) return true;
		}
		return false;
	}

	public boolean mouseReleased(Click click) {
		windows.forEach(window -> window.mouseReleased(scaleClick(click)));
		return false;
	}

	public boolean mouseDragged(Click click, double offsetX, double offsetY) {
		windows.forEach(window -> window.mouseDragged(scaleClick(click), offsetX, offsetY));
		return false;
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		windows.forEach(window -> window.mouseScrolled(mouseX / scale, mouseY / scale, horizontalAmount, verticalAmount));
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
		child.init();
	}

	@Override
	public List<Window> getChildren() {
		return windows;
	}

	@Override
	public List<Window> getAllChildren() {
		List<Window> list = new ArrayList<>(windows);
		windows.forEach((c) -> list.addAll(c.getAllChildren()));
		return list;
	}

	@Override
	public boolean alwaysOnTop() {
		return false;
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
	public WindowScreen getRootScreen() {
		return this;
	}

	@Override
	public int getWidth() {
		return (int) (width * scale);
	}

	@Override
	public void setWidth(int width) {

	}

	@Override
	public int getHeight() {
		return (int) (height * scale);
	}

	@Override
	public void setHeight(int height) {

	}

	@Override
	public int getInsideOffsetX() {
		return 0;
	}

	@Override
	public void setInsideOffsetX(int insideOffsetX) {

	}

	@Override
	public int getInsideOffsetY() {
		return 0;
	}

	@Override
	public void setInsideOffsetY(int insideOffsetY) {

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

	@Override
	public boolean shouldPause() {
		return false;
	}
}
