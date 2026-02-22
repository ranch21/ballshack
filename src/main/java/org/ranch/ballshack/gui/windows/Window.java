package org.ranch.ballshack.gui.windows;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
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

	public static final int NO_TITLE = 1;
	public static final int NO_BORDER = 2;
	public static final int NO_FILL = 4;
	public static final int NO_SCROLL = 8;
	public static final int NO_CLOSE = 16;

	protected Identifier CLOSE_TEXTURE = Identifier.of(BallsHack.ID, "textures/gui/close.png");

	private final List<Window> children = new ArrayList<>();
	private IWindow parent;
	private RemovalReason removalReason = null;

	private int x;
	private int y;

	private int width;
	private int height;

	private int insideOffsetX;
	private int insideOffsetY;

	public final int BAR_HEIGHT = 10;

	public final String title;

	public int style;

	private boolean focused;

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
		this.insideOffsetX = 0;
		this.insideOffsetY = 0;
		this.style = 0;
	}

	public void init() {

	}

	public void render(DrawContext context, double mouseX, double mouseY) {
		this.drawContext = context;

		if ((style & NO_BORDER) == 0)
			drawOutline(context, mouseX, mouseY);

		if ((style & NO_TITLE) == 0) {
			if ((style & NO_CLOSE) == 0 && GuiUtil.mouseOverlap(mouseX, mouseY, getX() + getWidth() - 7, getY() - BAR_HEIGHT + 2, 5, 5)) {
				context.setCursor(StandardCursors.POINTING_HAND);
			}
			drawTitle(context, mouseX, mouseY);
		}

		if ((style & NO_BORDER) == 0)
			drawBackground(context, mouseX, mouseY);

		if ((style & NO_SCROLL) == 0)
			drawScrollBars(context, mouseX, mouseY);

		context.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());

		children.removeIf(window -> window.getRemovalReason() != null);

		for (int i = children.size() - 1; i >= 0; i--) {
			children.get(i).render(context, mouseX, mouseY);
		}
		context.disableScissor();
	}

	protected void sortChildren() {
		children.sort((a, b) -> Boolean.compare(b.isFocused(), a.isFocused()));
		children.sort((a, b) -> Boolean.compare(b.alwaysOnTop(), a.alwaysOnTop()));
	}

	protected void drawTitle(DrawContext context, double mouseX, double mouseY) {
		DrawUtil.drawHorizontalGradient(context, getX(), getY() - BAR_HEIGHT, getWidth() - 1, BAR_HEIGHT, Colors.TITLE_START.getColor(), Colors.TITLE_END.getColor(), getWidth() / 10);
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		int textInset = (BAR_HEIGHT - textRend.fontHeight) / 2;
		context.drawText(textRend, title, getX() + 2, getY() + textInset - BAR_HEIGHT + 1, Color.WHITE.hashCode(), true);
		if ((style & NO_CLOSE) == 0) {
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
	}

	protected void drawScrollBars(DrawContext context, double mouseX, double mouseY) {
		if (needsScrollY()) {
			int maxScroll = getMaxScrollY();
			int scrollH = (int) (((float) getHeight() / maxScroll) * getHeight());
			int scrollP = (int) (((float) -getInsideOffsetY() / (maxScroll - getHeight())) * (getHeight() - scrollH));
			context.drawVerticalLine(
					getX() + getWidth() - 1,
					getY() + scrollP - 1,
					getY() + scrollP + scrollH,
					0xFF999999
			);
		}
		if (needsScrollX()) {
			int maxScroll = getMaxScrollX();
			int scrollW = (int) (((float) getWidth() / maxScroll) * getWidth());
			int scrollP = (int) (((float) -getInsideOffsetX() / (maxScroll - getWidth())) * (getWidth() - scrollW));
			context.drawHorizontalLine(
					getX() + scrollP - 1,
					getX() + scrollP + scrollW,
					getY() + getHeight() - 1,
					0xFF999999
			);
		}
	}

	protected void drawOutline(DrawContext context, double mouseX, double mouseY) {
		DrawUtil.drawOutline(context, getX(), getY() - ((style & NO_TITLE) == 0 ? BAR_HEIGHT : 0), getWidth(), getHeight() + ((style & NO_TITLE) == 0 ? BAR_HEIGHT : 0));
	}

	protected void drawBackground(DrawContext context, double mouseX, double mouseY) {
		context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Colors.FILL.getColor().hashCode());
	}

	public RemovalReason getRemovalReason() {
		return removalReason;
	}

	public void remove(RemovalReason reason) {
		this.removalReason = reason;
	}

	private boolean needsScrollX() {
		if ((style & NO_SCROLL) != 0) return false;
		for (Window window : children) {
			if (window.getX() - getInsideOffsetX() + window.getWidth() > getX() + getWidth()) return true;
		}
		return false;
	}

	public int getMaxScrollX() {
		int max = 0;
		for (Window window : children) {
			max = Math.max(max, window.getX() - getInsideOffsetX() + window.getWidth());
		}
		return Math.max(0, max - getX());
	}

	private boolean needsScrollY() {
		if ((style & NO_SCROLL) != 0) return false;
		for (Window window : children) {
			if (window.getY() - getInsideOffsetY() + window.getHeight() > getY() + getHeight()) return true;
		}
		return false;
	}

	public int getMaxScrollY() {
		int max = 0;
		for (Window window : children) {
			max = Math.max(max, window.getY() - getInsideOffsetY() + window.getHeight());
		}
		return Math.max(0, max - getY());
	}

	public void addFlags(int flags) {
		this.style |= flags;
	}

	public void removeFlags(int flags) {
		this.style &= ~(flags);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		children.forEach(window -> window.mouseMoved(mouseX, mouseY));
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if ((style & NO_CLOSE) == 0 && GuiUtil.mouseOverlap(click.x(), click.y(), getX() + getWidth() - 7, getY() - BAR_HEIGHT + 2, 5, 5) && (style & NO_TITLE) == 0) {
				remove(RemovalReason.CLOSED);
				setFocused(true);
				return true;
			} else if (GuiUtil.mouseOverlap(click.x(), click.y(), getX(), getY() - BAR_HEIGHT, getWidth(), BAR_HEIGHT) && (style & NO_TITLE) == 0) {
				dragging = true;
				dragX = (int) click.x() - getX();
				dragY = (int) click.y() - getY();
				setFocused(true);
				return true;
			}
		}
		if (GuiUtil.mouseOverlap(click.x(), click.y(), getX(), getY(), getWidth(), getHeight())) {
			setFocused(true);
			for (Window window : children) {
				window.mouseClicked(click, doubled);
			}
			return true;
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
		if (needsScrollX()) {
			insideOffsetX += (int) horizontalAmount * 2;
			insideOffsetX = Math.min(insideOffsetX, 0);
			insideOffsetX = Math.max(insideOffsetX, -getMaxScrollX());
		}
		if (needsScrollY()) {
			insideOffsetY += (int) verticalAmount * 2;
			insideOffsetY = Math.min(insideOffsetY, 0);
			insideOffsetY = Math.max(insideOffsetY, -getMaxScrollY() + getHeight());
		}

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

	protected void fill(int x1, int y1, int x2, int y2, int color) {
		drawContext.fill(getX() + x1, getY() + y1, getX() + x2, getY() + y2, color);
	}

	protected void text(String text, int x, int y, int color, boolean shadow) {
		text(Text.of(text), x, y, color, shadow);
	}

	protected void text(Text text, int x, int y, int color, boolean shadow) {
		drawContext.drawText(mc.textRenderer, text, getX() + x, getY() + y, color, shadow);
	}

	public boolean overlaps(Click click) {
		return overlaps(click.x(), click.y());
	}

	public boolean overlaps(double x, double y) {
		return GuiUtil.mouseOverlap(x, y, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void setFocused(boolean focused) {
		if (focused)
			getRoot().getAllChildren().forEach(window -> window.setFocused(false));
		this.focused = focused;
	}

	@Override
	public boolean isFocused() {
		return focused;
	}

	@Override
	public void addChild(Window child) {
		children.add(child);
		child.setParent(this);
		child.init();
	}

	@Override
	public List<Window> getChildren() {
		return children;
	}

	@Override
	public List<Window> getAllChildren() {
		List<Window> list = new ArrayList<>(children);
		children.forEach((c) -> list.addAll(c.getAllChildren()));
		return list;
	}

	@Override
	public boolean alwaysOnTop() {
		return false;
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
	public WindowScreen getRootScreen() {
		return getParent().getRootScreen();
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
	public int getInsideOffsetX() {
		return insideOffsetX;
	}

	@Override
	public void setInsideOffsetX(int insideOffsetX) {
		this.insideOffsetX = insideOffsetX;
	}

	@Override
	public int getInsideOffsetY() {
		return insideOffsetY;
	}

	@Override
	public void setInsideOffsetY(int insideOffsetY) {
		this.insideOffsetY = insideOffsetY;
	}

	@Override
	public int getX() {
		return x + parent.getX() + parent.getInsideOffsetX();
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y + parent.getY() + parent.getInsideOffsetY();
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}
}
