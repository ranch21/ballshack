package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;

import java.util.Map;

public class HelpCommand extends Command {
	public HelpCommand() {
		super("help", "Lists commands or gives command usage... i mean you just used it.");
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.executes(context -> {
					log("commands: ");
					for (Command command : CommandManager.getCommands()) {
						log(command.getName());
					}
					return 1;
				})
				.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("command", StringArgumentType.word())
						.suggests((ctx, builder2) -> {
							for (Command c : CommandManager.getCommands()) {
								builder2.suggest(c.getName());
							}
							return builder2.buildFuture();
						})
						.executes(context -> {
							Command c = CommandManager.getCommandByName(StringArgumentType.getString(context, "command"));

							if (c != null) {
								log(Text.literal(c.getName() + ": ").formatted(Formatting.WHITE).append(Text.literal(c.desc).formatted(Formatting.GRAY)));
								printUsage(c, context);
							}
							return 1;
						}));

	}

	private void printUsage(Command command, CommandContext<ClientCommandSource> context) {
		Map<CommandNode<ClientCommandSource>, String> map = CommandManager.getCommandDispatcher().getSmartUsage(
				command.onRegisterBase().build(), context.getSource()
		);

		for (String string : map.values()) {
			MutableText usagestr = Text.literal("Usage: ").withColor(BallsLogger.CMD_COLOR);
			MutableText name = Text.literal(CommandManager.prefix + command.getName() + " ").formatted(Formatting.WHITE);
			MutableText usage = Text.literal(string).formatted(Formatting.GRAY);
			log(usagestr.append(name.append(usage)));
		}
	}
}
