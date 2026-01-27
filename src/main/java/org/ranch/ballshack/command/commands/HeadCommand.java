package org.ranch.ballshack.command.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.util.InvUtil;
import org.ranch.ballshack.util.PlayerUtil;

public class HeadCommand extends Command {

	public static final SimpleCommandExceptionType MALFORMED_URL_EXCEPTION = new SimpleCommandExceptionType(Text.literal("Invalid name"));

	public HeadCommand() {
		super("head", "Gives head?", CommandType.CREATIVE);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.executes(context -> {
					InvUtil.createStack(getHead(mc.player.getGameProfile()));
					return 1;
				})
				.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("name", StringArgumentType.greedyString())
						.executes(context -> {
							String name = StringArgumentType.getString(context, "name");
							log(CMD(": ").append(Text.literal("Fetching...").formatted(Formatting.GRAY)));
							PlayerUtil.fetchProfile(name).thenCompose(PlayerUtil::fetchProperties).thenAccept(profile -> InvUtil.createStack(getHead(profile)));
							return 1;
						}));
	}

	private ItemStack getHead(GameProfile profile) {
		ItemStack heads = new ItemStack(Items.PLAYER_HEAD, 64);
		heads.set(DataComponentTypes.PROFILE, ProfileComponent.ofStatic(profile));
		return heads;
	}
}
