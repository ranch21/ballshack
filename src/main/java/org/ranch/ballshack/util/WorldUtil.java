package org.ranch.ballshack.util;

import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Objects;
import java.util.stream.Stream;

public class WorldUtil {

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static Stream<WorldChunk> getLoadedChunks() {
		int radius = Math.max(2, mc.options.getClampedViewDistance()) + 3;
		int diameter = radius * 2 + 1;

		ChunkPos center = mc.player.getChunkPos();
		ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
		ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);

		return Stream.iterate(min, pos -> {

					int x = pos.x;
					int z = pos.z;

					x++;

					if (x > max.x) {
						x = min.x;
						z++;
					}

					return new ChunkPos(x, z);

				}).limit((long) diameter * diameter)
				.filter(c -> mc.world.isChunkLoaded(c.x, c.z))
				.map(c -> mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);
	}

	public static InvType getInvType(BlockEntity b) {
		if (b instanceof TrappedChestBlockEntity) {
			return InvType.TRAPPED_CHEST;
		} else if (b instanceof EnderChestBlockEntity) {
			return InvType.ENDER_CHEST;
		} else if (b instanceof ChestBlockEntity) {
			return InvType.CHEST;
		} else if (b instanceof FurnaceBlockEntity) {
			return InvType.FURNACE;
		} else if (b instanceof ShulkerBoxBlockEntity) {
			return InvType.SHULKER_BOX;
		} else if (b instanceof BarrelBlockEntity) {
			return InvType.BARREL;
		} else {
			return null;
		}
	}

	public enum InvType {
		CHEST,
		ENDER_CHEST,
		TRAPPED_CHEST,
		FURNACE,
		SHULKER_BOX,
		BARREL
	}

	public static void placeBlock(Hand hand, BlockPos pos, Direction side) {
		if (mc.world == null || !mc.world.getWorldBorder().contains(pos) || !mc.world.getBlockState(pos).isSideSolidFullSquare(mc.world, pos, side) || !mc.world.getBlockState(pos.offset(side)).isReplaceable() || new Box(pos.offset(side)).intersects(mc.player.getBoundingBox()))
			return;

		ItemStack stack = mc.player.getStackInHand(hand);

		if (stack == null || stack == ItemStack.EMPTY || !stack.isItemEnabled(mc.world.getEnabledFeatures()))
			return;

		Item item = stack.getItem();

		if (!(item instanceof BlockItem))
			return;

		Vec3d faceMiddle = pos.toCenterPos().add(side.getDoubleVector().multiply(0.5d));
		BlockHitResult hitResult = new BlockHitResult(faceMiddle, side, pos, false);

		mc.interactionManager.interactBlock(mc.player, hand, hitResult);
		mc.player.swingHand(hand);
	}
}
