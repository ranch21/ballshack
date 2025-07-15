package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class Sprint extends Module {
	public Sprint() {
		super("Sprint", ModuleCategory.MOVEMENT, 0, "Run Forrest run");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		mc.player.setSprinting(
				mc.player.input.movementForward > 0 && !mc.player.isSneaking() && mc.player.getHungerManager().getFoodLevel() >= 6);
	}
}
