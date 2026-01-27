package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventClipLedge;
import org.ranch.ballshack.event.events.EventPlayerInput;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.PlayerSim;

public class SafeWalk extends Module {

	public final SettingToggle realAndTrue = new SettingToggle(false, "REALANDTRUE");

	public SafeWalk() {
		super("SafeWalk", ModuleCategory.MOVEMENT, 0, "Sneak un-sneakily");
	}

	private boolean shouldSneak = false;

	@EventSubscribe
	public void onClip(EventClipLedge event) {
		if (!realAndTrue.getValue()) {
			event.clip = true;
		} else {
			PlayerSim.PlayerPoint future = PlayerSim.simulatePlayer(mc.player);
			shouldSneak = !future.onGround() && mc.player.isOnGround();
		}
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerInput event) {
		if (shouldSneak) {
			event.sneak = true;
		}
	}
}
