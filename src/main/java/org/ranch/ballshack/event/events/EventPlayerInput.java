package org.ranch.ballshack.event.events;

import net.minecraft.util.PlayerInput;
import org.ranch.ballshack.event.Event;

public class EventPlayerInput extends Event {

	public final boolean forward;
	public final boolean backward;
	public final boolean left;
	public final boolean right;
	public final boolean jump;
	public boolean sneak;
	public final boolean sprint;

	public final boolean origForward;
	public final boolean origBackward;
	public final boolean origLeft;
	public final boolean origRight;
	public final boolean origJump;
	public final boolean origSneak;
	public final boolean origSprint;

	public EventPlayerInput(PlayerInput input) {
		this.forward = input.forward();
		this.backward = input.backward();
		this.left = input.left();
		this.right = input.right();
		this.jump = input.jump();
		this.sneak = input.sneak();
		this.sprint = input.sprint();
		// AAAAAAAAAAAAAAAAAAA
		this.origForward = input.forward();
		this.origBackward = input.backward();
		this.origLeft = input.left();
		this.origRight = input.right();
		this.origJump = input.jump();
		this.origSneak = input.sneak();
		this.origSprint = input.sprint();
	}

	public PlayerInput getInput() {
		return new PlayerInput(forward, backward, left, right, jump, sneak, sprint);
	}
}
