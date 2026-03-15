package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventClipLedge;
import org.ranch.ballshack.event.events.EventPlayerInput;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.BooleanSetting;
import org.ranch.ballshack.util.PlayerSim;

public class SafeWalk extends Module {

	public final BooleanSetting realAndTrue = dGroup.add(new BooleanSetting("Legit", false));

	public SafeWalk() {
		super("SafeWalk", ModuleCategory.MOVEMENT, 0, "Sneak un-sneakily");
	}

	private boolean shouldSneak = false;

	@EventSubscribe
	public void onClip(EventClipLedge event) {
		if (mc.player.isOnGround()) {
			event.clip = true;
		}
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerInput event) {
		if (shouldSneak) {
			event.sneak = true;
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (realAndTrue.getValue() && mc.player.isOnGround()) {
			int fallTick = PlayerSim.getFirst(PlayerSim.simulatePlayer(mc.player, 5), (point -> !point.onGround()));
			shouldSneak = fallTick < 3;
		} else {
			shouldSneak = false;
		}
	}
}
