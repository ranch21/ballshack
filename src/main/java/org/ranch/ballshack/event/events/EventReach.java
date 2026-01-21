package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventReach extends Event {

	public double reach;

	public EventReach(double reach) {
		this.reach = reach;
	}
}
