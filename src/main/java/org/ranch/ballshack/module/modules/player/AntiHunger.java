package org.ranch.ballshack.module.modules.player;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacketSend;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.mixin.PlayerMoveC2SPacketAccessor;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

public class AntiHunger extends Module {

	private boolean prevOnGround = false;
	private boolean ignorePacket = false;

	public SettingToggle sprint = dGroup.add(new SettingToggle(true, "Sprint"));
	public SettingToggle ground = dGroup.add(new SettingToggle(true, "Ground"));

	public AntiHunger() {
		super("AntiHunger", ModuleCategory.PLAYER, 0, "mmmmmmgghhh im soooo full");
	}

	@EventSubscribe
	public void onPacket(EventPacketSend event) {

		if (mc.player == null) return;

		if (event.packet instanceof PlayerMoveC2SPacket && ignorePacket) {
			ignorePacket = false;
			return;
		}

		if (mc.player.hasVehicle() || mc.player.isTouchingWater() || mc.player.isSubmergedInWater()) return;

		if (event.packet instanceof ClientCommandC2SPacket packet && sprint.getValue()) {
			if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) event.cancel();
		}

		if (ground.getValue() && event.packet instanceof PlayerMoveC2SPacket packet && mc.player.isOnGround() && mc.player.fallDistance <= 0.0 && !mc.interactionManager.isBreakingBlock()) {
			((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		if (mc.player.isOnGround() && !prevOnGround && ground.getValue()) {
			ignorePacket = true;
		}

		prevOnGround = mc.player.isOnGround();
	}
}
