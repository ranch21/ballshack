package org.ranch.ballshack.module.modules.player;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacketSend;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.mixin.PlayerMoveC2SPacketAccessor;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.util.Arrays;

public class AntiHunger extends Module {

	private boolean prevOnGround = false;
	private boolean ignorePacket = false;

	public AntiHunger() {
		super("AntiHunger", ModuleCategory.PLAYER, 0, new ModuleSettings(Arrays.asList(
				new SettingToggle(true, "Sprint"),
				new SettingToggle(true, "Ground")
		)));
	}

	@EventSubscribe
	public void onPacket(EventPacketSend event) {

		if (mc.player == null) return;

		boolean sprint = (boolean) settings.getSetting(0).getValue();
		boolean onGround = (boolean) settings.getSetting(1).getValue();

		if (event.packet instanceof PlayerMoveC2SPacket && ignorePacket) {
			ignorePacket = false;
			return;
		}

		if (mc.player.hasVehicle() || mc.player.isTouchingWater() || mc.player.isSubmergedInWater()) return;

		if (event.packet instanceof ClientCommandC2SPacket packet && sprint) {
			if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) event.cancel();
		}

		if (onGround && event.packet instanceof PlayerMoveC2SPacket packet && mc.player.isOnGround() && mc.player.fallDistance <= 0.0 && !mc.interactionManager.isBreakingBlock()) {
			((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		boolean onGround = (boolean) settings.getSetting(1).getValue();

		if (mc.player.isOnGround() && !prevOnGround && onGround) {
			ignorePacket = true;
		}

		prevOnGround = mc.player.isOnGround();
	}
}
