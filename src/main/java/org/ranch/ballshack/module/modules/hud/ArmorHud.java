package org.ranch.ballshack.module.modules.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.util.InvUtil;

import java.util.List;

public class ArmorHud extends ModuleHud {
	public ArmorHud() {
		super("ArmorHud", ModuleCategory.HUD, 0, 50, -39, "inbdfgnjnojlouijmijlkmijlkmijlkmijkjwwajjsdnuijksdanjnjdwadnwajkd", ModuleAnchor.BOTTOM_CENTER);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender.Post event) {
		int x = X() - xOffset();
		int y = Y() - yOffset();
		DrawContext context = event.drawContext;

		PlayerEntity player = mc.player;

		List<ItemStack> armorItems = InvUtil.getArmorSlots(player);
		int j = 0;
		for (int i = armorItems.size() - 1; i >= 0; i--) {
			context.drawItem(armorItems.get(i), x + (j * 16) - 1, y - 1);
			context.drawStackOverlay(mc.textRenderer, armorItems.get(i), x + (j * 16) - 1, y - 1);
			j++;
		}
		width = 4 * 16;
		height = 15;
	}
}
