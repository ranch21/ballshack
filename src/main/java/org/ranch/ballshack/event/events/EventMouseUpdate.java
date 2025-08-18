package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventMouseUpdate extends Event {

	public double deltaX;
	public double deltaY;

	public final double origDeltaX;
	public final double origDeltaY;

	public final double timeDelta;

	public EventMouseUpdate(double deltaX, double deltaY, double timeDelta) {
		this.origDeltaX = deltaX;
		this.origDeltaY = deltaY;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.timeDelta = timeDelta;
	}
}
