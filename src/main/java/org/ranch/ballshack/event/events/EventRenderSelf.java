package org.ranch.ballshack.event.events;

import org.ranch.ballshack.event.Event;

public class EventRenderSelf extends Event {
	public boolean render;
	public final boolean origRender;

	public EventRenderSelf(boolean render) {
		this.origRender = this.render = render;
	}
}
