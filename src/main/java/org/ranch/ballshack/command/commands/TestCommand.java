package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.command.Command;

public class TestCommand extends Command {
	public TestCommand() {
		super("test", "testing", "test");
	}

	@Override
	public void onCall(int argc, String[] argv) {
		BallsLogger.info(getName() + " called");
	}
}
