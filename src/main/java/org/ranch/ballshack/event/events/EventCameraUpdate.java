package org.ranch.ballshack.event.events;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.Event;

public class EventCameraUpdate extends Event {

	public Vec3d pos;
	public final Vec3d origPos;
	public final float tickProgress;

	public EventCameraUpdate(Vec3d pos, float tickProgress) {
		this.pos = pos;
		this.origPos = pos;
		this.tickProgress = tickProgress;
	}
}
