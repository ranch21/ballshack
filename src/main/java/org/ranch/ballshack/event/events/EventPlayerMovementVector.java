package org.ranch.ballshack.event.events;

import net.minecraft.util.math.Vec2f;
import org.ranch.ballshack.event.Event;

public class EventPlayerMovementVector extends Event {

	public Vec2f movement;
	public static Vec2f origMovement;

	public EventPlayerMovementVector(Vec2f movement) {
		this.movement = movement;
		this.origMovement = movement;
	}
}
