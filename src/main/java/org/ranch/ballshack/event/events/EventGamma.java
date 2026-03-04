package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventGamma extends Event {
	public final double origGamma;
	public double gamma;

	public EventGamma(double gamma) {
		this.origGamma = this.gamma = gamma;
	}
}
