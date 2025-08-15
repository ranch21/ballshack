package org.ranch.ballshack.module.modules.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.setting.HudModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;
import java.util.Arrays;

public class Coordinates extends ModuleHud {
	public Coordinates() {
		super("Coords", ModuleCategory.HUD, 0, 0, 0, new HudModuleSettings(Arrays.asList(
				new SettingToggle(true, "Backdrop"),
				new SettingToggle(true, "Shadow"),
				new SettingToggle(false, "Scaled")
		)), "f3 wgat is it", ModuleAnchor.TOP_LEFT);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender event) {
		int x = X();
		int y = Y();
		DrawContext context = event.drawContext;

		boolean shadow = (boolean) getSettings().getSetting(1).getValue();

		PlayerEntity player = mc.player;

		MutableText data = Text.of(player.getBlockX() + " ").copy().withColor(Colors.RED.hashCode());
		data.append(Text.of(player.getBlockY() + " ").copy().withColor(Colors.GREEN.hashCode()));
		data.append(Text.of(player.getBlockZ() + " ").copy().withColor(Colors.BLUE.hashCode()));

		width = mc.textRenderer.getWidth(data);
		height = mc.textRenderer.fontHeight;

		context.fill(x, y, x + width, y + height, Colors.BACKDROP.hashCode());

		DrawUtil.drawText(context, mc.textRenderer, data, x, y, Color.WHITE, true);

		/*if (isOnRight()) {
			DrawUtil.drawTextRight(context, mc.textRenderer, data, x, y, Color.WHITE, true);
		} else {
			DrawUtil.drawText(context, mc.textRenderer, data, x, y, Color.WHITE, true);
		}*/
	}
}
