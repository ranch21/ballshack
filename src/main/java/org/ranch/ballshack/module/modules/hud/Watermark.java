package org.ranch.ballshack.module.modules.hud;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.StringSetting;

import java.awt.*;

import static org.ranch.ballshack.util.TextUtil.applyFormatting;

public class Watermark extends ModuleHud {

	public final BooleanSetting backdrop = dGroup.add(new BooleanSetting("Backdrop", true));
	public final BooleanSetting shadow = dGroup.add(new BooleanSetting("Shadow", true));
	public final StringSetting text = dGroup.add(new StringSetting("Text", "$watermark $bhversion $mcversion"));

	public Watermark() {
		super("Watermark", ModuleCategory.HUD, 0, 0, 0, "inbdfgnjnojlouijmijlkmijlkmijlkmijkjwwajjsdnuijksdanjnjdwadnwajkd", ModuleAnchor.TOP_LEFT);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender.Post event) {
		int x = X() - xOffset();
		int y = Y() - yOffset();
		DrawContext context = event.drawContext;

		String text = applyFormatting(this.text.getValue());
		width = mc.textRenderer.getWidth(text) + 2;
		height = mc.textRenderer.fontHeight + 1;

		if (backdrop.getValue())
			context.fill(x, y, x + width, y + height, Colors.CLICKGUI_BACKGROUND_2.getColor().hashCode());

		context.drawText(mc.textRenderer, text, x + 1, y + 1, Color.WHITE.hashCode(), shadow.getValue());
	}
}
