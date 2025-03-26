package org.ranch.ballshack.gui.window.windows;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.gui.window.Window;
import org.ranch.ballshack.gui.window.widgets.TextFieldWidget;
import org.ranch.ballshack.setting.SettingSaver;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;

public class SettingsWindow extends Window {

	public SettingsWindow(int x, int y) {
		super(x, y, 150, 100, "Settings");
	}

	@Override
	public void setup() {
		widgets.add(new TextFieldWidget(60, 5, 80, 10, BallsHack.title.getValue()));
		//widgets.add(new TextFieldWidget(5, 20, 50, 10));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, Screen screen) {
		super.render(context, mouseX, mouseY, screen);
		super.render(context, mouseX, mouseY, screen);
		TextRenderer textRend = MinecraftClient.getInstance().textRenderer;
		DrawUtil.drawText(context,textRend, "Watermark", 6 + x, 6 + y, Color.WHITE, true);
		BallsHack.title.setValue(((TextFieldWidget)widgets.get(0)).getText());
		SettingSaver.SCHEDULE_SAVE.set(true);
	}
}
