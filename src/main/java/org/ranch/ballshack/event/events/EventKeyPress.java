package org.ranch.ballshack.event.events;

import net.minecraft.client.input.KeyInput;
import org.ranch.ballshack.event.Event;

public class EventKeyPress extends Event {

	public KeyInput input;

	public EventKeyPress(KeyInput input) {
		this.input = input;
	}
}
