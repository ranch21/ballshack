package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

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

	public static List<ItemStack> getArmorSlots(LivingEntity entity) {
		List<ItemStack> armorItems = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			armorItems.add(entity.getEquippedStack(ARMOR_SLOTS[i]));
		}
		return armorItems;
	}

	// i know man
	public static void setSlot(ItemStack stack, int slot) {
		if (mc.player.isInCreativeMode() && mc.getNetworkHandler().hasFeature(stack.getItem().getRequiredFeatures())) {
			mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(slot, stack));
			mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.playerScreenHandler.syncId));
		}
	}

	public static void giveItem(ItemStack stack) {
		setSlot(stack, getFree());
	}

	public static int getFree() {
		for (int i = 1; i < mc.player.getInventory().size(); i++) {
			if (mc.player.getInventory().getStack(i).isEmpty())
				return i;
		}

		return -1;
	}

	public static void selectSlot(int slot) {
		mc.player.getInventory().setSelectedSlot(slot);
		mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
	}
}
