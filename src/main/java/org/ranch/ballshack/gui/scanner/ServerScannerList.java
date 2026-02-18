package org.ranch.ballshack.gui.scanner;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.util.DatabaseFetcher;

import java.util.List;

public class ServerScannerList extends ServerList {

	private List<DatabaseFetcher.Server> servers = Lists.newArrayList(); // AAARAGGh
	private DatabaseFetcher.ServerFilters serverFilters = new DatabaseFetcher.ServerFilters();

	public ServerScannerList(MinecraftClient client) {
		super(client);
	}

	@Override
	public void loadFile() {
		servers.clear();
		servers = DatabaseFetcher.getServers(serverFilters);
		BallsLogger.info("Loaded " + servers.size() + " servers");
	}

	public void setServerFilters(DatabaseFetcher.ServerFilters serverFilters) {
		this.serverFilters = serverFilters;
	}

	@Override
	public void saveFile() {

	}

	@Override
	public ScannedServerInfo get(int index) {
		return servers.get(index).getServerInfo();
	}

	@Override
	public ScannedServerInfo get(String address) {
		for (DatabaseFetcher.Server server : servers) {
			if (!server.getCombinedString().equals(address)) continue;
			return server.getServerInfo();
		}
		return null;
	}

	@Override
	public void remove(ServerInfo serverInfo) {

	}

	@Override
	public void add(ServerInfo serverInfo, boolean hidden) {

	}

	@Override
	public int size() {
		return servers.size();
	}

	@Override
	public void swapEntries(int index1, int index2) {

	}

	@Override
	public void set(int index, ServerInfo serverInfo) {

	}
}
