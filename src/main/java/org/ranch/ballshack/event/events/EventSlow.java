package org.ranch.ballshack.event.events;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.Event;

public class EventSlow extends Event {

	public final Vec3d origMult;
	public Vec3d mult;

	public EventSlow(Vec3d mult) {
		this.origMult = this.mult = mult;
	}
}
