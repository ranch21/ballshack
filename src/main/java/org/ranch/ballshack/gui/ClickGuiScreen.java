package org.ranch.ballshack.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;

import java.util.List;

public class ClickGuiScreen extends Screen {

	ButtonWidget button;
	List<ButtonWidget> buttons;

	public ClickGuiScreen() {
		super(NarratorManager.EMPTY);
	}

	protected void init() {
		Text text = Text.literal("test");
		this.button = this.addDrawableChild(ButtonWidget.builder(text, (button) -> {
			System.out.println("test");
		}).build());
	}

	public boolean shouldPause() {
		return false;
	}
}
