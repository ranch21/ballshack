package org.ranch.ballshack.util;

import net.minecraft.SharedConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.module.ModuleManager;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.ranch.ballshack.BallsHack.mc;

@SuppressWarnings("DataFlowIssue")
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

		addFormatter("svr_ip", () -> {
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

		addFormatter("svr_address", () -> {
			try {
				if (mc.isConnectedToLocalServer()) {
					return "SinglePlayer";
				} else {
					return InetAddress.getByName(mc.getNetworkHandler().getServerInfo().address).getHostAddress();
				}
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("svr_brand", () -> {
			try {
				if (mc.isConnectedToLocalServer()) {
					return "vanilla";
				} else {
					return mc.getNetworkHandler().getBrand();
				}
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("day", () -> {
			try {
				return String.valueOf(mc.world.getTimeOfDay() / 24000L);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("svr_ping", () -> {
			try {
				return String.valueOf(mc.getNetworkHandler().getPlayerListEntry(BallsHack.mc.player.getGameProfile().id()).getLatency());
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("svr_motd", () -> {
			try {
				return mc.getNetworkHandler().getServerInfo().label.getString();
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("svr_protocol", () -> {
			try {
				return String.valueOf(mc.getNetworkHandler().getServerInfo().protocolVersion);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("svr_version", () -> {
			try {
				return mc.getNetworkHandler().getServerInfo().version.getString();
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("difficulty", () -> {
			try {
				return mc.world.getDifficulty().name();
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
				double dx = player.getX() - player.lastX;
				double dy = player.getY() - player.lastY;
				double dz = player.getZ() - player.lastZ;
				return String.format("%.1f", Math.sqrt(dx * dx + dy * dy + dz * dz) * 20);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("fps", () -> {
			try {
				return String.valueOf(BallsHack.mc.getCurrentFps());
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("modules", () -> {
			try {
				ArrayList<String> modules = new ArrayList<>();
				ModuleManager.getModules().forEach(module -> {
					if (module.isEnabled() && !module.isMeta()) modules.add(module.getName());
				});
				return String.join(" ", modules);
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("bhversion", () -> {
			try {
				return BallsHack.version;
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("mcversion", () -> {
			try {
				return SharedConstants.getGameVersion().name();
			} catch (Throwable t) {
				return "unknown";
			}
		});

		addFormatter("watermark", () -> {
			try {
				return BallsHack.title.getValue();
			} catch (Throwable t) {
				return "unknown";
			}
		});
	}
}
