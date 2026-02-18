package org.ranch.ballshack.gui.scanner;

import net.minecraft.client.network.ServerInfo;

public class ScannedServerInfo extends ServerInfo {
	public boolean canJoin;
	public boolean modded;

	public ScannedServerInfo(String name, String address, ServerType serverType, boolean canJoin, boolean modded) {
		super(name, address, serverType);
		this.canJoin = canJoin;
		this.modded = modded;
	}
}
