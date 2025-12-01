package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.RangedWeaponItem;

import java.util.ArrayList;
import java.util.List;

public class InvUtil {

	public static final int SLOTS = 36;
	public static final int HOTSLOTS = 9;
	public static final int OFFHAND = 45;

	public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static int interactionToInv(int slot) {
		return slot >= 36 ? slot - 36 : slot;
	}

	public static boolean isThrowable(Item item) {
		return item instanceof RangedWeaponItem || item instanceof ProjectileItem;
	}

	public static List<ItemStack> getArmorSlots(PlayerEntity player) {
		List<ItemStack> armorItems = new ArrayList<ItemStack>();
		for (int i = 0; i < 4; i++) {
			armorItems.add(player.getEquippedStack(ARMOR_SLOTS[i]));
		}
		return armorItems;
	}
}
