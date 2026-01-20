package org.ranch.ballshack.module.modules.player;

import net.minecraft.util.math.Vec2f;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class AutoWalk extends Module {
	public AutoWalk() {
		super("AutoWalk", ModuleCategory.PLAYER, 0, "Walk but i- ok ill shut up");
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerMovementVector event) {
		event.movement = new Vec2f(event.movement.x, 1); // yeah thats what i wouldve did
		// they really be making filler modules
	}
}
