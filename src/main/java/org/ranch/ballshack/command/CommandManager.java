package org.ranch.ballshack.command;

import org.ranch.ballshack.command.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

	public static String prefix = ".";

	static List<Command> commands = new ArrayList<Command>(Arrays.asList(
			new TestCommand(),
			new GPTCommand(),
			new PrefixCommand(),
			new FriendCommand(),
			new DebugRenderersCommand()
	));

	public static void onCommand(String command) {
		String[] tokens = command.split(" ");
		for (Command c : commands) {
			if (c.getName().equalsIgnoreCase(tokens[0])) {
				c.onCall(tokens);
			}
		}
	}

	public static Command getCommandByName(String name) {
		for (Command command : commands) {
			if (command.getName().equals(name)) {
				return command;
			}
		}
		return null;
	}
}
