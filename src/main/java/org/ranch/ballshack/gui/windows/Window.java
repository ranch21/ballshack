package org.ranch.ballshack.gui.windows;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Window implements IWindow, Element {

	protected Identifier CLOSE_TEXTURE = Identifier.of(BallsHack.ID, "textures/gui/close.png");

	private final List<Window> children = new ArrayList<>();
	private IWindow parent;
	private RemovalReason removalReason = null;

	private int x;
	private int y;

	private int width;
	private int height;

	public final int BAR_HEIGHT = 10;

	public final String title;

	public boolean hasBorder;
	public boolean hasTitle;
	public boolean fillBackground;

	protected boolean dragging;
	protected int dragX;
	protected int dragY;

	protected DrawContext drawContext;

	protected final MinecraftClient mc = MinecraftClient.getInstance();

	public Window(String title, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.title = title;
		this.width = width;
		this.height = height;
		this.hasBorder = true;
		this.hasTitle = true;
		this.fillBackground = true;
	}

	public void render(DrawContext context, double mouseX, double mouseY) {

		this.drawContext = context;

		handleDrag(mouseX, mouseY);

		if (hasBorder)
			DrawUtil.drawOutline(context, getX(), getY() - (hasTitle ? BAR_HEIGHT : 0), getWidth(), getHeight() + (hasTitle ? BAR_HEIGHT : 0));

		if (hasTitle)
			drawTitle(context);

		if (fillBackground)
			context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Colors.CLICKGUI_2.getColor().hashCode());

		context.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());

		children.removeIf(window -> window.getRemovalReason() != null);

		for (Window window : children) {
			window.render(context, mouseX, mouseY);
		}

		context.disableScissor();
	}

	private void drawTitle(DrawContext context) {
		DrawUtil.drawHorizontalGradient(context, getX(), getY() - BAR_HEIGHT, getWidth() - 1, BAR_HEIGHT, Colors.CLICKGUI_TITLE_START.getColor(), Colors.CLICKGUI_TITLE_END.getColor(), getWidth() / 10);
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (BAR_HEIGHT - textRend.fontHeight) / 2;
		context.drawText(textRend, title, getX() + 2, getY() + textInset - BAR_HEIGHT, Color.WHITE.hashCode(), true);
		context.drawTexture(
				RenderPipelines.GUI_TEXTURED,
				CLOSE_TEXTURE,
				getX() + getWidth() - 7,
				getY() - BAR_HEIGHT + 2,
				0, 0,
				5, 5,
				5, 5
		);
	}

	public RemovalReason getRemovalReason() {
		return removalReason;
	}

	public void remove(RemovalReason reason) {
		this.removalReason = reason;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		children.forEach(window -> window.mouseMoved(mouseX, mouseY));
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (GuiUtil.mouseOverlap(click.x(), click.y(), getX() + getWidth() - 7, getY() - BAR_HEIGHT + 2, 5, 5)) {
				remove(RemovalReason.CLOSED);
			} else if (GuiUtil.mouseOverlap(click.x(), click.y(), getX(), getY() - BAR_HEIGHT, getWidth(), BAR_HEIGHT)) {
				dragging = true;
				dragX = (int) click.x() - getX();
				dragY = (int) click.y() - getY();
				return true;
			} else if (GuiUtil.mouseOverlap(click.x(), click.y(), getX(), getY(), getWidth(), getHeight())) {
				for (Window window : children) {
					if (window.mouseClicked(click, doubled)) return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Click click) {
		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			dragging = false;
		}
		children.forEach(window -> window.mouseReleased(click));
		return false;
	}

	@Override
	public boolean mouseDragged(Click click, double offsetX, double offsetY) {
		children.forEach(window -> window.mouseDragged(click, offsetX, offsetY));
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		children.forEach(window -> window.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
		return false;
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		children.forEach(window -> window.keyPressed(input));
		return false;
	}

	@Override
	public boolean keyReleased(KeyInput input) {
		children.forEach(window -> window.keyReleased(input));
		return false;
	}

	@Override
	public boolean charTyped(CharInput input) {
		children.forEach(window -> window.charTyped(input));
		return false;
	}

	protected void handleDrag(double mouseX, double mouseY) {
		if (dragging) {
			x = (int) (mouseX - dragX);
			y = (int) (mouseY - dragY);
		}

		/*x = Math.max(x, 0);
		y = Math.max(y, BAR_HEIGHT);
		x = Math.min(x, parent.getWidth() - width);
		y = Math.min(y, parent.getHeight());*/
	}

	protected void fill(int x1, int y1, int x2, int y2, int color) {
		drawContext.fill(getX() + x1, getY() + y1, x2, y2, color);
	}

	protected void text(String text, int x, int y, int color, boolean shadow) {
		drawContext.drawText(mc.textRenderer, text, getX() + x, getY() + y, color, shadow);
	}

	public boolean overlaps(Click click) {
		return GuiUtil.mouseOverlap(click.x(), click.y(), getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void setFocused(boolean focused) {

	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	public void addChild(Window child) {
		children.add(child);
		child.setParent(this);
	}

	@Override
	public void setParent(IWindow parent) {
		this.parent = parent;
	}

	@Override
	public IWindow getParent() {
		return parent;
	}

	@Override
	public IWindow getRoot() {
		return parent.getRoot();
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getX() {
		return x + parent.getX();
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y + parent.getY();
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}
}
