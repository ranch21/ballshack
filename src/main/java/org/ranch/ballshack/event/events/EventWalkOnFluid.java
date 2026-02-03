package org.ranch.ballshack.event.events;

import net.minecraft.fluid.FluidState;
import org.ranch.ballshack.event.Event;

public class EventWalkOnFluid extends Event {

	public boolean can;
	public final FluidState fluidState;

	public EventWalkOnFluid(boolean can, FluidState fluidState) {
		this.can = can;
		this.fluidState = fluidState;
	}
}
