package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventClipLedge;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class SafeWalk extends Module {
	public SafeWalk() {
		super("SafeWalk", ModuleCategory.MOVEMENT, 0, "Sneak un-sneakily");
	}

	@EventSubscribe
	public void onClip(EventClipLedge event) {
		event.clip = true;
	}
}
