package org.ranch.ballshack.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsLogger;

public abstract class Command {

	protected final MinecraftClient mc = MinecraftClient.getInstance();

	protected String name;
	public final String usage; // eg. prefix <prefix> or help <module> | help
	public final String desc;

	public Command(String name, String desc, String usage) {
		this.name = name;
		this.desc = desc;
		this.usage = usage;
	}

	public String getName() {
		return name;
	}

	public abstract void onCall(int argc, String[] argv);

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
