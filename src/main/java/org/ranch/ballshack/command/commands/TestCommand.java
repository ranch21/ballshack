package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;

public class TestCommand extends Command {
	public TestCommand() {
		super("testBalls", "testing", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder.executes(context -> {
			log("hello");
			return 1;
		});
	}
}
