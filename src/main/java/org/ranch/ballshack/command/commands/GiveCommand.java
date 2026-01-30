package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.util.InvUtil;

public class GiveCommand extends Command {

	public GiveCommand() {
		super("give", "Gives item", CommandType.CREATIVE);
	}

	private CommandRegistryAccess getRegistry() {
		return CommandRegistryAccess.of(mc.world.getRegistryManager(), mc.world.getEnabledFeatures());
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(RequiredArgumentBuilder.<ClientCommandSource, ItemStackArgument>argument("item", ItemStackArgumentType.itemStack(getRegistry()))
						.executes(context -> {
							ItemStackArgument stackArg = ItemStackArgumentType.getItemStackArgument(context, "item");
							InvUtil.giveItem(stackArg.createStack(1, false));
							return 1;
						})
						.then(RequiredArgumentBuilder.<ClientCommandSource, Integer>argument("count", IntegerArgumentType.integer(1))
								.executes(context -> {
									ItemStackArgument stackArg = ItemStackArgumentType.getItemStackArgument(context, "item");
									InvUtil.giveItem(stackArg.createStack(IntegerArgumentType.getInteger(context, "count"), false));
									return 1;
								})));
	}
}
