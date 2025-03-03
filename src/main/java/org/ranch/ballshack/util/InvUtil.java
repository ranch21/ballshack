package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;

public class InvUtil {

	public static final int SLOTS = 36;
	public static final int HOTSLOTS = 9;
	public static final int OFFHAND = 45;

	private static MinecraftClient mc = MinecraftClient.getInstance();

	public static int interactionToInv(int slot) {
		return slot >= 36 ? slot - 36 : slot;
	}
}
