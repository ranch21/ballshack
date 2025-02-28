package org.ranch.ballshack.setting.settings;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.setting.ModuleSetting;

public class SettingSlider extends ModuleSetting<Integer> {
	public SettingSlider(int startingValue, String name) {
		super(name, startingValue);
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta) {

	}
}
