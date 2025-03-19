package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventClipLedge extends Event {

	public boolean clip;

	public EventClipLedge(boolean clip) {
		this.clip = clip;
	}
}
