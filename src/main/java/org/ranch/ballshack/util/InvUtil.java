package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.RangedWeaponItem;

public class InvUtil {

	public static final int SLOTS = 36;
	public static final int HOTSLOTS = 9;
	public static final int OFFHAND = 45;

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static int interactionToInv(int slot) {
		return slot >= 36 ? slot - 36 : slot;
	}

	public static boolean isThrowable(Item item) {
		return item instanceof RangedWeaponItem || item instanceof ProjectileItem;
	}
}
