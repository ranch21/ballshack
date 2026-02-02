package org.ranch.ballshack.command;

import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.command.commands.*;
import org.ranch.ballshack.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

	private static final List<Command> commands = new ArrayList<>();

	public static final SimpleCommandExceptionType CREATIVE_EXCEPTION = new SimpleCommandExceptionType(Text.literal("Command requires creative!"));

	public static final Setting<Character> prefix = new Setting<>('.', "prefix", new TypeToken<Character>() {
	}.getType());

	private static @Nullable CommandDispatcher<ClientCommandSource> dispatcher;

	public static CommandDispatcher<ClientCommandSource> getCommandDispatcher() {
		return dispatcher;
	}

	public static void setCommandDispatcher(CommandDispatcher<ClientCommandSource> dispatcher) {
		CommandManager.dispatcher = dispatcher;
	}

	private static void registerCommandBuilder(LiteralArgumentBuilder<ClientCommandSource> command) {
		if (dispatcher == null)
			return;
		dispatcher.register(command);
	}

	private static void registerCommand(Command command) {
		registerCommandBuilder(command.onRegisterBase());
		commands.add(command);
	}

	public static void registerCommands() {
		//registerCommand(new TestCommand());
		registerCommand(new HelpCommand());
		registerCommand(new PrefixCommand());
		registerCommand(new FriendCommand());
		registerCommand(new DebugRenderersCommand());
		registerCommand(new BindCommand());
		registerCommand(new SettingCommand());
		registerCommand(new FormatCommand());
		registerCommand(new ServerCommand());
		registerCommand(new WatermarkCommand());
		registerCommand(new ThemeCommand());
		registerCommand(new SearchCommand());

		registerCommand(new ToggleCommand());

		registerCommand(new ClearInvCommand());
		registerCommand(new RenameCommand());
		registerCommand(new HeadCommand());
		registerCommand(new GiveCommand());
	}

	public static void executeCommand(String command, ClientCommandSource source) {
		if (dispatcher == null)
			return;
		try {
			final ParseResults<ClientCommandSource> parse = dispatcher.parse(command, source);
			String root = parse.getContext()
					.getNodes()
					.get(0)
					.getNode()
					.getName();
			Command c = getCommandByName(root);

			if (c != null && c.type == CommandType.CREATIVE && BallsHack.mc.interactionManager.getCurrentGameMode() != GameMode.CREATIVE) {
				throw CREATIVE_EXCEPTION.createWithContext(parse.getReader());
			}

			dispatcher.execute(command, source);
		} catch (CommandSyntaxException e) {
			BallsLogger.error(e.getMessage());
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
