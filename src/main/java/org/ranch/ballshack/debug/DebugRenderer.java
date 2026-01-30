package org.ranch.ballshack.debug;

import org.ranch.ballshack.BallsHack;

public abstract class DebugRenderer {

	private boolean enabled;

	public DebugRenderer() {
		this.enabled = false;
	}

	public void setEnabled(boolean enabled) {
		if (enabled)
			BallsHack.eventBus.subscribe(this);
		else
			BallsHack.eventBus.unsubscribe(this);
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return enabled;
	}
}
