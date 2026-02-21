package org.ranch.ballshack.module.modules.player;

import net.minecraft.client.gui.screen.DeathScreen;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class AutoRespawn extends Module {
	public AutoRespawn() {
		super("AutoRespawn", ModuleCategory.PLAYER, 0, "Respawns in an automatic fashion");
	}

	@EventSubscribe
	public void onScreenInit(EventScreen.Init event) {
		if (mc.world == null) return;

		if (event.screen instanceof DeathScreen && mc.world.getLevelProperties().isHardcore() && mc.player != null) {
			mc.player.requestRespawn();
		}
	}
}
