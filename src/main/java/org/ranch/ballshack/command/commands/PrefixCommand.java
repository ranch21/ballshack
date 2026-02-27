package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.command.arguments.CharacterArgumentType;

public class PrefixCommand extends Command {
	public PrefixCommand() {
		super("prefix", "Sets the command prefix.", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(RequiredArgumentBuilder.<ClientCommandSource, Character>argument("prefix", new CharacterArgumentType())
						.executes(context -> {
							CommandManager.prefix.setValue(CharacterArgumentType.getCharacter(context, "prefix"));
							return 1;
						}));
	}
}
