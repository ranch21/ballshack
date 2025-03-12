package org.ranch.ballshack.event;

public class Event {

	private boolean cancelled;

	public void cancel() {
		this.cancelled = true;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
}
