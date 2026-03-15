package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public abstract class EventWorld extends Event {

	public static class Join extends EventWorld {
	}

	public static class Leave extends EventWorld {
	}
}
