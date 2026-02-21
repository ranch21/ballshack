package org.ranch.ballshack.module.modules.combat;

import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.util.InvUtil;

public class AutoTotem extends Module {
	public AutoTotem() {
		super("AutoTotem", ModuleCategory.COMBAT, 0, "God mode (requires totems to run)");
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		if (mc.player == null || mc.interactionManager == null)
			return;

		boolean holdingTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;

		if (holdingTotem || !mc.player.getOffHandStack().isEmpty()) {
			return;
		}

		if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
			for (int i = 0; i < InvUtil.SLOTS + InvUtil.HOTSLOTS; i++) {

				int slot = InvUtil.interactionToInv(i);

				if (mc.player.getInventory().getStack(slot).getItem() == Items.TOTEM_OF_UNDYING) {
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
					mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, InvUtil.OFFHAND, 0, SlotActionType.PICKUP, mc.player);
				}
			}
		}
	}
}
