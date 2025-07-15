package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventSetSneaking extends Event {

	public boolean sneaking;

	public EventSetSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}
}
