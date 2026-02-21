package org.ranch.ballshack.gui.windows;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.realms.util.TextRenderingUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.joml.Matrix3x2fStack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.gui.windows.widgets.TextWidget;
import org.ranch.ballshack.util.TextUtil;

public class ConsoleWindow extends Window {

	private static final float SCALE = 1F;

	private int lastMsgCount = 0;

	public ConsoleWindow(String title, int x, int y, int width, int height) {
		super(title, x, y, width, height);
	}

	private void syncMessages() {
		if (BallsLogger.recentMessages.size() == lastMsgCount) return;

		getChildren().clear();
		int currentY = 5;

		for (Text line : BallsLogger.recentMessages.reversed()) {
			for (OrderedText orderedText : mc.textRenderer.wrapLines(line, (int) ((getWidth() - 5) / SCALE))) {
				MutableText t = Text.empty();
				orderedText.accept((index, style, codePoint) -> {
					t.append(Text.literal(new String(Character.toChars(codePoint))).setStyle(style));
					return true;
				});
				TextWidget widget = new TextWidget(t, 5, currentY, 0, 0);
				addChild(widget);
				currentY += mc.textRenderer.fontHeight + 1;
			}
		}

		lastMsgCount = BallsLogger.recentMessages.size();
		scrollToBottom();
	}

	private void scrollToBottom() {
		int maxScroll = getMaxScrollY();
		if (maxScroll > getHeight()) {
			setInsideOffsetY(-(maxScroll - getHeight() + mc.textRenderer.fontHeight));
		}
	}

	@Override
	public void render(DrawContext context, double mouseX, double mouseY) {
		syncMessages();
		super.render(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, double mouseX, double mouseY) {
		fill(0, 0, getWidth(), getHeight(), 0xFF000000);
	}
}
