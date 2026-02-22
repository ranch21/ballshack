package org.ranch.ballshack;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.ranch.ballshack.BallsHack.mc;

@SuppressWarnings("DataFlowIssue")
public class BallsLogger {
	public static final Logger logger = LogManager.getFormatterLogger("BallsHack");

	public static final Deque<Text> recentMessages = new ArrayDeque<>();
	private static final Queue<Text> queuedInGameLogs = new ConcurrentLinkedQueue<>();
	public static final int maxHistMessages = 100;

	private static final Text ballsText = Text.literal("BallsHack");

	public static final int BH_COLOR = Formatting.GOLD.getColorValue();

	public static final int CMD_COLOR = Formatting.GOLD.getColorValue();

	public static final int INFO_COLOR = Formatting.GRAY.getColorValue();
	public static final int WARN_COLOR = Formatting.YELLOW.getColorValue();
	public static final int ERROR_COLOR = Formatting.RED.getColorValue();

	private static void addToHistory(String message) {
		recentMessages.addFirst(Text.of(message));
		if (recentMessages.size() > maxHistMessages) {
			recentMessages.removeLast();
		}
	}

	private static void addToHistory(Text message) {
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
		queuedInGameLogs.add(addBallText(info, INFO_COLOR));
		addToHistory(info);
	}

	public static void warn(Object warn) {
		warn(Text.literal(String.valueOf(warn)));
	}

	public static void warn(String warn) {
		warn(Text.literal(warn));
	}

	public static void warn(Text warn) {
		logger.warn(warn.getString());
		queuedInGameLogs.add(addBallText(warn, WARN_COLOR));
		addToHistory(warn);
	}

	public static void error(Object error) {
		error(Text.literal(String.valueOf(error)));
	}

	public static void error(String error) {
		error(Text.literal(error));
	}

	public static void error(Text error) {
		logger.info(error.getString());
		queuedInGameLogs.add(addBallText(error, ERROR_COLOR));
		addToHistory(error);
	}

	public static void onTick() {
		for (Text msg : queuedInGameLogs) {
			mc.player.sendMessage(msg, false);
		}
		queuedInGameLogs.clear();
	}
}
