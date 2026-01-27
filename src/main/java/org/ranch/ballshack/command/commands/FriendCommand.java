package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.suggestors.PlayerSuggestor;

import java.util.List;

public class FriendCommand extends Command {
	public FriendCommand() {
		super("friends", "Used to modify friends list");
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("add")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("name", StringArgumentType.string())
								.suggests(new PlayerSuggestor())
								.executes(context -> {
									String name = StringArgumentType.getString(context, "name");
									if (FriendManager.add(name)) {
										log(CMD(": ").append(Text.literal("Added friend " + name).formatted(Formatting.GRAY)));
									}
									return 1;
								})))

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("remove")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("name", StringArgumentType.string())
								.suggests(new PlayerSuggestor())
								.executes(context -> {
									String name = StringArgumentType.getString(context, "name");
									if (FriendManager.remove(name)) {
										log(CMD(": ").append(Text.literal("Removed friend " + name).formatted(Formatting.GRAY)));
									}
									return 1;
								})))

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("list")
						.executes(context -> {
							List<String> friendsList = FriendManager.getFriends();
							if (friendsList.isEmpty()) {
								log(CMD(": ").append(Text.literal("No friends :(").formatted(Formatting.GRAY)));
								return 1;
							}
							MutableText flist = Text.literal("");
							log(CMD(": ").append(Text.literal("Friends: ").formatted(Formatting.GRAY)));
							for (String friend : friendsList) {
								if (!flist.getString().isEmpty()) {
									flist.append(Text.literal(", "));
								}
								flist.append(Text.literal(friend));
							}
							log(flist.formatted(Formatting.GRAY));
							return 1;
						}))

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("clear")
						.executes(context -> {
							FriendManager.clear();
							log(CMD(": ").append("Cleared friends").formatted(Formatting.GRAY));
							return 1;
						}));
	}
}
