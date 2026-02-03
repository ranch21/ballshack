package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventCameraExtend extends Event {

	public float f, g, h;
	public final float origF, origG, origH;

	public EventCameraExtend(float f, float g, float h) {
		this.origF = this.f = f;
		this.origG = this.g = g;
		this.origH = this.h = h;
	}
}
