package org.ranch.ballshack.event.events;

import net.minecraft.network.packet.Packet;
import org.ranch.ballshack.event.Event;

public class EventPacket extends Event {

	public final Packet<?> packet;

	public EventPacket(Packet<?> packet) {
		this.packet = packet;
	}

	public static class Send extends EventPacket {
		public Send(Packet<?> packet) {
			super(packet);
		}
	}

	public static class Receive extends EventPacket {
		public Receive(Packet<?> packet) {
			super(packet);
		}
	}
}
