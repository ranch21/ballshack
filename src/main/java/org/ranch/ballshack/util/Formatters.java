package org.ranch.ballshack.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.ranch.ballshack.BallsHack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.ranch.ballshack.BallsHack.mc;

public class Formatters {
	public static final Map<String, Supplier<String>> FORMATTERS = new LinkedHashMap<>();
	public static final String PREFIX = "$";

	private static void addFormatter(String key, Supplier<String> value) {
		FORMATTERS.put(PREFIX + key, value);
	}

	static {
		addFormatter("gamemode", () -> {
			try {
				GameMode gm = mc.interactionManager.getCurrentGameMode();
				if (gm == null) return "unknown";
				String s = gm.toString().toLowerCase();
				return s.substring(0, 1).toUpperCase() + s.substring(1);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("hp", () -> {
			try {
				float hp = mc.player.getHealth();
				return String.valueOf((int) hp);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("x", () -> {
			try {
				double x = mc.player.getX();
				return String.valueOf((int) x);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("y", () -> {
			try {
				double y = mc.player.getY();
				return String.valueOf((int) y);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("z", () -> {
			try {
				double z = mc.player.getZ();
				return String.valueOf((int) z);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("name", () -> {
			try {
				Text name = mc.player.getName();
				return name.getString();
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("ip", () -> {
			try {
				if (mc.isConnectedToLocalServer()) {
					return "SinglePlayer";
				} else {
					return mc.getNetworkHandler().getServerInfo().address;
				}
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("players", () -> {
			try {
				if (mc.isConnectedToLocalServer()) {
					return "1";
				} else {
					return String.valueOf(mc.getNetworkHandler().getServerInfo().players.online());
				}
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("max_players", () -> {
			try {
				if (mc.isConnectedToLocalServer()) {
					return "1";
				} else {
					return String.valueOf(mc.getNetworkHandler().getServerInfo().players.max());
				}
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("bps", () -> {
			try {
				PlayerEntity player = mc.player;
				double dx = player.getX() - player.prevX;
				double dy = player.getY() - player.prevY;
				double dz = player.getZ() - player.prevZ;
				return String.format("%.1f", Math.sqrt(dx * dx + dy * dy + dz * dz) * 20);
			} catch (Throwable t) {
				return "unknown";
			}
		});
	}
}
