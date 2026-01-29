package org.ranch.ballshack.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.hit.BlockHitResult;

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
		List<ItemStack> armorItems = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			armorItems.add(player.getEquippedStack(ARMOR_SLOTS[i]));
		}
		return armorItems;
	}

	// idunno man
	public static void createStack(ItemStack stack) {
		if (mc.player.isInCreativeMode() && mc.getNetworkHandler().hasFeature(stack.getItem().getRequiredFeatures())) {
			mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(-1, stack));
			mc.player.getInventory().setStack(-1, stack.copy());
		}
	}

	public static void selectSlot(int slot) {
		mc.player.getInventory().setSelectedSlot(slot);
		mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
	}
}
