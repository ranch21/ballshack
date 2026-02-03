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
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.awt.*;

public class Coordinates extends ModuleHud {

	public final SettingToggle backdrop = dGroup.add(new SettingToggle("Backdrop", true));
	public final SettingToggle shadow = dGroup.add(new SettingToggle("Shadow", true));
	public final SettingToggle scaled = dGroup.add(new SettingToggle("Scaled", false));

	public Coordinates() {
		super("Coords", ModuleCategory.HUD, 0, 0, 0, "f3 wgat is it", ModuleAnchor.BOTTOM_LEFT);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender event) {
		int x = X() - xOffset();
		int y = Y() - yOffset();
		DrawContext context = event.drawContext;

		PlayerEntity player = mc.player;

		MutableText data = Text.of(player.getBlockX() + " ").copy().withColor(Colors.DULL_RED.hashCode());
		data.append(Text.of(player.getBlockY() + " ").copy().withColor(Colors.DULL_GREEN.hashCode()));
		data.append(Text.of(player.getBlockZ() + " ").copy().withColor(Colors.DULL_BLUE.hashCode()));

		if (scaled.getValue()) {
			float mult = mc.world.getDimension().ultrawarm() ? 8.0f : 0.125f;
			data.append(Text.of("[").copy().withColor(Colors.DULL_GRAY.hashCode()));
			data.append(Text.of((int) (player.getBlockX() * mult) + " ").copy().withColor(Colors.DULL_RED.hashCode()));
			data.append(Text.of((int) (player.getBlockY() * mult) + " ").copy().withColor(Colors.DULL_GREEN.hashCode()));
			data.append(Text.of((int) (player.getBlockZ() * mult) + " ").copy().withColor(Colors.DULL_BLUE.hashCode()));
			data.append(Text.of("]").copy().withColor(Colors.DULL_GRAY.hashCode()));
		}

		width = mc.textRenderer.getWidth(data) - 2;
		height = mc.textRenderer.fontHeight + 1;

		if (backdrop.getValue()) context.fill(x, y, x + width, y + height, Colors.CLICKGUI_BACKGROUND_2.getColor().hashCode());

		context.drawText(mc.textRenderer, data, x + 1, y + 1, Color.WHITE.hashCode(), shadow.getValue());
	}
}
