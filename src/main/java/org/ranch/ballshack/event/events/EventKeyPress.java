package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventKeyPress extends Event {

	private int keyCode;

	public EventKeyPress(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKey() {
		return keyCode;
	}
}
