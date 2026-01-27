package org.ranch.ballshack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

public class BallsLogger {
	public static Logger logger = LogManager.getFormatterLogger("BallsHack");

	public static Deque<String> recentMessages = new ArrayDeque<>();
	public static final int maxHistMessages = 100;

	private static final Text ballsText = Text.literal("BallsHack");

	public static int BH_COLOR = Formatting.GREEN.getColorValue();

	public static int CMD_COLOR = Formatting.GOLD.getColorValue();

	public static int INFO_COLOR = Formatting.AQUA.getColorValue();
	public static int WARN_COLOR = Formatting.YELLOW.getColorValue();
	public static int ERROR_COLOR = Formatting.RED.getColorValue();

	private static void addToHistory(String message) {
		recentMessages.addFirst(message);
		if (recentMessages.size() > maxHistMessages) {
			recentMessages.removeLast();
		}
	}

	public static Text addBallText(Text text, int color) {
		return ballsText.copy().styled(s -> s.withColor(BH_COLOR)).append(": ").append(text.copy().styled(s -> s.withColor(color)));
	}

	public static void info(Object info) {
		info(Text.literal(String.valueOf(info)));
	}

	public static void info(String info) {
		info(Text.literal(info));
	}

	public static void info(Text info) {
		logger.info(info.getString());
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			player.sendMessage(addBallText(info, INFO_COLOR), false);
		}
		addToHistory(info.toString());
	}

	public static void warn(Object warn) {
		warn(Text.literal(String.valueOf(warn)));
	}

	public static void warn(String warn) {
		warn(Text.literal(warn));
	}

	public static void warn(Text warn) {
		logger.warn(warn.getString());
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			player.sendMessage(addBallText(warn, WARN_COLOR), false);
		}
		addToHistory(warn.toString());
	}

	public static void error(Object error) {
		error(Text.literal(String.valueOf(error)));
	}

	public static void error(String error) {
		error(Text.literal(error));
	}

	public static void error(Text error) {
		logger.info(error.getString());
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			player.sendMessage(addBallText(error, ERROR_COLOR), false);
		}
		addToHistory(error.toString());
	}
}
