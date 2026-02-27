package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public abstract class EventMouseUpdate extends Event {

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

	public static class Mouse extends EventMouseUpdate {
		public Mouse(double deltaX, double deltaY, double timeDelta) {
			super(deltaX, deltaY, timeDelta);
		}
	}

	public static class Entity extends EventMouseUpdate {
		public Entity(double deltaX, double deltaY) {
			super(deltaX, deltaY, 1);
		}
	}
}
