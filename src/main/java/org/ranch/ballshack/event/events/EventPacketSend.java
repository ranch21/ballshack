package org.ranch.ballshack.event.events;

import net.minecraft.network.packet.Packet;
import org.ranch.ballshack.event.Event;

public class EventPacketSend extends Event {

	public final Packet<?> packet;

	public EventPacketSend(Packet<?> packet) {
		this.packet = packet;
	}
}
