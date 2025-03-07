package org.ranch.ballshack.module.modules;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class NoFall extends Module {
	public NoFall() {
		super("NoFall", ModuleCategory.PLAYER, 0);
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		if (mc.player.isCreative()) return;

		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, mc.player.horizontalCollision));

	}
}
