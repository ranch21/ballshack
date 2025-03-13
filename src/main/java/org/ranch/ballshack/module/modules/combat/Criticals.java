package org.ranch.ballshack.module.modules.combat;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacketSend;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class Criticals extends Module {
	public Criticals() {
		super("Criticals", ModuleCategory.COMBAT, 0);
	}

	@EventSubscribe
	public void onPacketSent(EventPacketSend event) {
		if (mc.player == null) return;

		if (event.packet instanceof PlayerInteractEntityC2SPacket packet) {
			sendPacket(0.062);
			sendPacket(0);
			sendPacket(0.072);
			sendPacket(0);
		}
	}

	public void sendPacket(double height) {
		Vec3d pos = mc.player.getPos();

		PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y + height, pos.z, false, mc.player.horizontalCollision);
		mc.player.networkHandler.sendPacket(packet);
	}
}
