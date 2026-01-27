package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;

public class RenameCommand extends Command {
	public RenameCommand() {
		super("rename", "Renames held item.", CommandType.CREATIVE);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("name", StringArgumentType.greedyString())
						.executes(context -> {
							String name = StringArgumentType.getString(context, "name");
							ItemStack held = mc.player.getMainHandStack();
							held.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name));
							return 1;
						}));
	}
}
