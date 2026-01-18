package org.ranch.ballshack.module.modules.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.setting.HudModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingString;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.InvUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.ranch.ballshack.util.TextUtil.applyFormatting;

public class Watermark extends ModuleHud {
	public Watermark() {
		super("Watermark", ModuleCategory.HUD, 0, 0, 0, new HudModuleSettings(Arrays.asList(
				new SettingToggle(true, "Backdrop"),
				new SettingToggle(true, "Shadow"),
				new SettingString("Text", "$watermark $bhversion $mcversion", 256)
		)), "inbdfgnjnojlouijmijlkmijlkmijlkmijkjwwajjsdnuijksdanjnjdwadnwajkd", ModuleAnchor.BOTTOM_CENTER);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender event) {
		int x = X() - xOffset();
		int y = Y() - yOffset();
		DrawContext context = event.drawContext;

		boolean backdrop = (boolean) getSettings().getSetting(0).getValue();
		boolean shadow = (boolean) getSettings().getSetting(1).getValue();

		String text = applyFormatting((String)getSettings().getSetting(2).getValue());
		width = mc.textRenderer.getWidth(text)+2;
		height = mc.textRenderer.fontHeight+1;

		if (backdrop) context.fill(x, y, x + width, y + height, Colors.BACKDROP.hashCode());

		DrawUtil.drawText(context, mc.textRenderer, text, x + 1, y + 1, Color.WHITE, shadow);
	}
}
