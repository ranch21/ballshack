package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.render.Search;

public class SearchCommand extends Command {
	public SearchCommand() {
		super("search", "sersv.", CommandType.CLIENT);
	}

	private CommandRegistryAccess getRegistry() {
		return CommandRegistryAccess.of(mc.world.getRegistryManager(), mc.world.getEnabledFeatures());
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {

		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("add")
					.then(RequiredArgumentBuilder.<ClientCommandSource, BlockStateArgument>argument("block", BlockStateArgumentType.blockState(getRegistry()))
							.executes(context -> {
								Search search = (Search) ModuleManager.getModuleByName("search");
								Identifier id = Registries.BLOCK.getId(context.getArgument("block", BlockStateArgument.class).getBlockState().getBlock());
								search.blocks.getValue().add(id);
								search.reload();
								log(CMD(": ").append(Text.literal("Added " + id).formatted(Formatting.GRAY)));
								return 0;
							})))
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("remove")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("block", StringArgumentType.greedyString())
								.suggests((context, builder2) -> {
									for (Identifier id : ((Search) ModuleManager.getModuleByName("search")).blocks.getValue()) {
										builder2.suggest(id.toString());
									}
									return builder2.buildFuture();
								})
								.executes(context -> {
									Search search = (Search) ModuleManager.getModuleByName("search");
									Identifier id = Identifier.of(StringArgumentType.getString(context, "block"));
									if (search.blocks.getValue().contains(id)) {
										search.blocks.getValue().remove(id);
										search.reload();
										log(CMD(": ").append(Text.literal("Removed " + id).formatted(Formatting.GRAY)));
									} else {
										log(CMD(": ").append(Text.literal("Block not on list").formatted(Formatting.GRAY)));
									}
									return 0;
								})))
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("clear")
								.executes(context -> {
									Search search = (Search) ModuleManager.getModuleByName("search");
									search.blocks.getValue().clear();
									search.reload();
									log(CMD(": ").append(Text.literal("Cleared blocks").formatted(Formatting.GRAY)));
									return 0;
								}))
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("list")
						.executes(context -> {
							Search search = (Search) ModuleManager.getModuleByName("search");
							log(CMD(": "));
							for (Identifier id : search.blocks.getValue()) {
								log(Text.literal(id.toString()).formatted(Formatting.GRAY));
							}
							return 0;
						}));
	}
}
