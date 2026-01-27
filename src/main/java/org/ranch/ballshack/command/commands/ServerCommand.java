package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.util.Formatters;

public class ServerCommand extends Command {
	public ServerCommand() {
		super("server", "Dump server info.", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {

		return builder
				.executes(context -> {
					log(getDataLine("svr_ip"));
					log(getDataLine("svr_address"));
					log(getDataLine("svr_brand"));
					log(getDataLine("svr_ping"));
					log(getDataLine("svr_motd"));
					log(getDataLine("svr_protocol"));
					log(getDataLine("svr_version"));
					log(getDataLine("day"));
					log(getDataLine("difficulty"));
					log(getDataLine("players"));
					log(getDataLine("max_players"));
					return 1;
				});
	}

	private MutableText getDataLine(String formatter) {
		MutableText data = Text.literal(Formatters.FORMATTERS.get(Formatters.PREFIX + formatter).get()).formatted(Formatting.GRAY);
		MutableText name = Text.literal(formatter + ": ").withColor(BallsLogger.CMD_COLOR);
		return name.append(data);
	}
}
