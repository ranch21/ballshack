package org.ranch.ballshack.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsLogger;

public abstract class Command {

	protected final MinecraftClient mc = MinecraftClient.getInstance();

	protected final String name;
	public final String desc;
	public final CommandType type;

	public Command(String name, String desc, CommandType type) {
		this.name = name;
		this.desc = desc;
		this.type = type;
	}

	public LiteralArgumentBuilder<ClientCommandSource> onRegisterBase() {
		return onRegister(LiteralArgumentBuilder.literal(name));
	}

	public abstract LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder);


	public String getName() {
		return name;
	}

	protected void log(String message) {
		log(Text.literal(message));
	}

	protected void log(MutableText message) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			player.sendMessage(message, false);
		}
	}

	protected MutableText CMD() {
		return Text.literal(getName()).withColor(BallsLogger.CMD_COLOR);
	}

	protected MutableText CMD(String string) {
		return Text.literal(getName() + string).withColor(BallsLogger.CMD_COLOR);
	}
}
