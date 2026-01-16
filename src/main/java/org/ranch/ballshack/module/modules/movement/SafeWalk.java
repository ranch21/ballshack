package org.ranch.ballshack.module.modules.movement;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventClipLedge;
import org.ranch.ballshack.event.events.EventPlayerInput;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.PlayerSim;

import java.util.Arrays;

public class SafeWalk extends Module {
	public SafeWalk() {
		super("SafeWalk", ModuleCategory.MOVEMENT, 0, new ModuleSettings(Arrays.asList(
				new SettingToggle(false, "REALANDTRUE"))
		), "Sneak un-sneakily");
	}

	private boolean shouldSneak = false;

	@EventSubscribe
	public void onClip(EventClipLedge event) {
		if (!(boolean) settings.getSetting(0).getValue()) {
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
