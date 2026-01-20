package org.ranch.ballshack.command;

import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.command.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

	public static String prefix = ".";

	static final List<Command> commands = new ArrayList<>(Arrays.asList(
			new TestCommand(),
			new PrefixCommand(),
			new FriendCommand(),
			new DebugRenderersCommand(),
			new HelpCommand(),
			new BindCommand()
	));

	public static void onCommand(String command) {
		String[] tokens = command.split(" ");
		for (Command c : commands) {
			if (c.getName().equalsIgnoreCase(tokens[0])) {
				c.onCall(tokens.length, tokens);
				return;
			}
		}
		BallsLogger.info("no such thing buddy, tell jeff to add it.. or use .help");
	}

	public static Command getCommandByName(String name) {
		for (Command command : commands) {
			if (command.getName().equals(name)) {
				return command;
			}
		}
		return null;
	}

	public static List<Command> getCommands() {
		return commands;
	}
}
