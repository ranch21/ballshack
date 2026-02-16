package org.ranch.ballshack.gui.legacy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import org.ranch.ballshack.gui.legacy.window.LegacyCategoryWindow;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LegacyClickGuiScreen extends Screen {

	ButtonWidget button;
	final List<LegacyCategoryWindow> windows = new ArrayList<>();

	public LegacyClickGuiScreen() {
		super(NarratorManager.EMPTY);
	}

	private void loadCategories() {
		int i = 0;
		for (ModuleCategory category : ModuleCategory.values()) {
			windows.add(new LegacyCategoryWindow((60 * i) + 10, 10, category.name(), true, category));
			i++;
		}
	}

	protected void init() {
		windows.clear(); //NOTLEGACY
		loadCategories();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		for (LegacyCategoryWindow window : windows) {
			window.render(context, mouseX, mouseY, delta, this);
		}

		MinecraftClient mc = MinecraftClient.getInstance(); //NOTLEGACY
		context.fill(width - 50, height - 10, width, height, Color.BLUE.getRGB()); //NOTLEGACY
		context.drawText(mc.textRenderer, "go back", width - 50, height - 10, Color.WHITE.hashCode(), true);//NOTLEGACY

	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {

		if (LegacyGuiUtil.mouseOverlap(click.x(), click.y(), width - 50, height - 10, 50, 10)) { //NOTLEGACY
			((ClickGui) ModuleManager.getModuleByName("ClickGui")).legacy.setValue(false); //NOTLEGACY
		} //NOTLEGACY

		for (LegacyCategoryWindow window : windows) {
			window.mouseClicked(click.x(), click.y(), click.button());
		}

		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean mouseReleased(Click click) {

		for (LegacyCategoryWindow window : windows) {
			window.mouseReleased(click.x(), click.y(), click.button());
		}

		return super.mouseReleased(click);
	}

	public boolean shouldPause() {
		return false;
	}
}
