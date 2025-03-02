package org.ranch.ballshack.event.events;

import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.event.Event;

public class EventWorldRender extends Event {

	public final MatrixStack matrices;
	public final float tickDelta;

	public EventWorldRender(MatrixStack matrices, float tickDelta) {
		this.matrices = matrices;
		this.tickDelta = tickDelta;
	}
}
