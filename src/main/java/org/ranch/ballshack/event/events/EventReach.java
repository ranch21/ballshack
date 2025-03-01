package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventReach extends Event {

	private float reach;

	public EventReach(float reach) {
		this.reach = reach;
	}

	public float getReach() {
		return reach;
	}

	public void setReach(float reach) {
		this.reach = reach;
	}
}
