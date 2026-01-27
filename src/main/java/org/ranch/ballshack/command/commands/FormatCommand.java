package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.command.suggestors.FormatSuggestor;
import org.ranch.ballshack.util.Formatters;
import org.ranch.ballshack.util.TextUtil;

import java.util.Map;
import java.util.function.Supplier;

public class FormatCommand extends Command {
	public FormatCommand() {
		super("format", "Formats a string.", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("list")
						.executes(context -> {
							for (Map.Entry<String, Supplier<String>> formatter : Formatters.FORMATTERS.entrySet()) {
								log(formatter.getKey());
							}
							return 1;
						}))
				.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("string", StringArgumentType.greedyString())
						.suggests(new FormatSuggestor())
						.executes(context -> {
							String string = StringArgumentType.getString(context, "string");
							log(TextUtil.applyFormatting(string));
							return 1;
						}));
	}
}
