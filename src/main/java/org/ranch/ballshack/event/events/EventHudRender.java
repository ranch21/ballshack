package org.ranch.ballshack.event.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.ranch.ballshack.event.Event;

public abstract class EventHudRender extends Event {

	public final DrawContext drawContext;
	public final RenderTickCounter tickCounter;

	public EventHudRender(DrawContext context, RenderTickCounter tickCounter) {
		this.drawContext = context;
		this.tickCounter = tickCounter;
	}

	public static class Pre extends EventHudRender {
		public Pre(DrawContext context, RenderTickCounter tickCounter) {
			super(context, tickCounter);
		}
	}

	public static class Post extends EventHudRender {
		public Post(DrawContext context, RenderTickCounter tickCounter) {
			super(context, tickCounter);
		}
	}
}
