package org.ranch.ballshack.module.modules.client;

import net.minecraft.network.packet.Packet;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacket;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class PacketLogger extends Module {

	public PacketLogger() {
		super("PacketLogger", ModuleCategory.CLIENT, 0, "wgat am i sending");
	}

	private Packet<?> last;

	@EventSubscribe
	public void onPacketSend(EventPacket.Send event) {
		if (last != event.packet)
			BallsLogger.info(event.packet.getClass().getName()); // todo make this not ass

		last = event.packet;
	}

	@EventSubscribe
	public void onPacketSend(EventPacket.Receive event) {
		if (last != event.packet)
			BallsLogger.info(event.packet.getClass().getName());

		last = event.packet;
	}
}
