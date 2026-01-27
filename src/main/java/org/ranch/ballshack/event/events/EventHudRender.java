package org.ranch.ballshack.event.events;

import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.event.Event;

public class EventHudRender extends Event {

	public final DrawContext drawContext;
	public final float tickDelta;

	public EventHudRender(DrawContext context, float tickDelta) {
		this.drawContext = context;
		this.tickDelta = tickDelta;
	}
}
