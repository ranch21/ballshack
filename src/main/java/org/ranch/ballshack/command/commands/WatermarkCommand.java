package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.setting.ModuleSettingSaver;

public class WatermarkCommand extends Command {

	public WatermarkCommand() {
		super("watermark", "Set the watermark text.", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("watermark", StringArgumentType.string())
						.executes(context -> {
							String watermark = StringArgumentType.getString(context, "watermark");

							BallsHack.title.setValue(watermark);
							return 1;
						}));
	}
}
