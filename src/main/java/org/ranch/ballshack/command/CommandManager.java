package org.ranch.ballshack.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.network.ClientCommandSource;
import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.command.commands.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

	private static List<Command> commands = new ArrayList<>();

	public static char prefix = '.';

	private static @Nullable CommandDispatcher<ClientCommandSource> dispatcher;

	public static CommandDispatcher<ClientCommandSource> getCommandDispatcher() {
		return dispatcher;
	}

	public static void setCommandDispatcher(CommandDispatcher<ClientCommandSource> dispatcher) {
		CommandManager.dispatcher = dispatcher;
	}

	private static void registerCommandBuilder(LiteralArgumentBuilder<ClientCommandSource> command) {
		dispatcher.register(command);
	}

	private static Command registerCommand(Command command) {
		registerCommandBuilder(command.onRegisterBase());
		commands.add(command);
		return command;
	}

	public static void registerCommands() {
		registerCommand(new TestCommand());
		registerCommand(new HelpCommand());
		registerCommand(new PrefixCommand());
		registerCommand(new FriendCommand());
		registerCommand(new DebugRenderersCommand());
		registerCommand(new BindCommand());
		registerCommand(new ToggleCommand());
	}

	public static boolean executeCommand(String command, ClientCommandSource source) {
		try {
			dispatcher.execute(command, source);
			return true;
		} catch (CommandSyntaxException e) {
			BallsLogger.error(e.getContext());
			return true;
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

	public static List<Command> getCommands() {
		return commands;
	}
}
