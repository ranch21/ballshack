package org.ranch.ballshack.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsLogger;

public abstract class Command {

	protected final MinecraftClient mc = MinecraftClient.getInstance();

	protected String name;

	public Command(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract void onCall(String[] args);

	private Text getCommandText(Text text, int color) {
		return Text.literal(name).copy().styled(s -> s.withColor(BallsLogger.BH_COLOR)).append(": ").append(text.copy().styled(s -> s.withColor(color)));
	}

	protected void log(String message, boolean prependName) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			if (prependName) {
				player.sendMessage(getCommandText(Text.literal(message), BallsLogger.INFO_COLOR), false);
			} else {
				player.sendMessage(Text.literal(message).copy().styled(s -> s.withColor(BallsLogger.INFO_COLOR)), false);
			}
		}
	}

}
