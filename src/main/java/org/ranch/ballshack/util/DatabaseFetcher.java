package org.ranch.ballshack.util;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.network.ServerInfo;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.gui.scanner.ScannedServerInfo;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.SettingsManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFetcher {

	public static final Setting<String> host = new Setting<>("", "db-host", new TypeToken<String>() {
	}.getType());
	public static final Setting<String> user = new Setting<>("", "db-user", new TypeToken<String>() {
	}.getType());
	public static final Setting<String> password = new Setting<>("", "db-password", new TypeToken<String>() {
	}.getType());

	public record Server(String address, int port, boolean canJoin, boolean modded, int id) {
		public ScannedServerInfo getServerInfo() {
			return new ScannedServerInfo("#" + id, getCombinedString(), ServerInfo.ServerType.OTHER, canJoin, modded);
		}

		public String getCombinedString() {
			return address + ":" + port;
		}
	}

	public static void registerSettings() {
		SettingsManager.registerSetting(host);
		SettingsManager.registerSetting(user);
		SettingsManager.registerSetting(password);
	}

	public static List<Server> getServers(ServerFilters filters) {
		List<Server> servers = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://" + host.getValue() + ":5432/servers", user.getValue(), password.getValue())) {
			Statement statement = connection.createStatement();
			String query = "SELECT address, port, canjoin, modded, id FROM servers";
			if (!filters.getClause().isEmpty()) {
				query = query + " " + filters.getClause();
			}
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String address = resultSet.getString("address");
				int port = resultSet.getInt("port");
				boolean canJoin = resultSet.getBoolean("canjoin");
				boolean modded = resultSet.getBoolean("modded");
				int id = resultSet.getInt("id");
				servers.add(new Server(address, port, canJoin, modded, id));
			}
			BallsLogger.info("Fetched " + servers.size() + " servers from database");
		} catch (SQLException e) {
			BallsLogger.error(e);
		}

		return servers;
	}

	public static class ServerFilters {

		public enum FilterBool {
			TRUE, FALSE, ANY;

			public boolean active() {
				return this != ANY;
			}

			public boolean asBool() {
				return this != FALSE;
			}

			public FilterBool cycle() {
				int i = this.ordinal();
				if (i >= values().length - 1) {
					return values()[0];
				} else {
					return values()[i + 1];
				}
			}
		}

		private FilterBool modded;
		private FilterBool canjoin;
		private int minPlayers;
		private int maxPlayers;

		private String clause;

		public ServerFilters() {
			modded = FilterBool.ANY;
			canjoin = FilterBool.ANY;
			minPlayers = 0;
			maxPlayers = Integer.MAX_VALUE;
			clause = null;
		}

		public ServerFilters minPlayers(int minPlayers) {
			this.minPlayers = minPlayers;
			return this;
		}

		public ServerFilters maxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
			return this;
		}

		public ServerFilters modded(FilterBool modded) {
			this.modded = modded;
			return this;
		}

		public ServerFilters canjoin(FilterBool canjoin) {
			this.canjoin = canjoin;
			return this;
		}

		public ServerFilters setClause(String clause) {
			this.clause = clause;
			return this;
		}

		public String getClause() {
			if (clause == null) {
				generateClause();
			}

			if (clause == null) {
				return "";
			} else {
				return clause;
			}
		}

		private void generateClause() {
			List<String> conditions = new ArrayList<>();
			boolean shouldFilter = false;

			if (minPlayers >= 0 && minPlayers < Integer.MAX_VALUE) {
				conditions.add("playercount >= " + minPlayers);
				shouldFilter = true;
			}

			if (maxPlayers >= 0 && maxPlayers < Integer.MAX_VALUE) {
				conditions.add("playercount <= " + maxPlayers);
				shouldFilter = true;
			}

			if (modded.active()) {
				conditions.add("modded = " + (modded.asBool() ? "true" : "false"));
				shouldFilter = true;
			}

			if (canjoin.active()) {
				conditions.add("canjoin = " + (canjoin.asBool() ? "true" : "false"));
				shouldFilter = true;
			}

			if (shouldFilter) {
				StringBuilder stringBuilder = new StringBuilder("WHERE");
				boolean firstCondition = true;
				for (String condition : conditions) {
					if (!firstCondition) {
						stringBuilder.append(" AND");
					}
					stringBuilder.append(" ").append(condition);
					firstCondition = false;
				}
				clause = stringBuilder.toString();
			} else {
				clause = null;
			}
		}
	}
}
