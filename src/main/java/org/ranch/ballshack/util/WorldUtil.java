package org.ranch.ballshack.util;

import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Objects;
import java.util.stream.Stream;

public class WorldUtil {

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static Stream<WorldChunk> getLoadedChunks()
	{
		int radius = Math.max(2, mc.options.getClampedViewDistance()) + 3;
		int diameter = radius * 2 + 1;

		ChunkPos center = mc.player.getChunkPos();
		ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
		ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);

		Stream<WorldChunk> stream = Stream.<ChunkPos> iterate(min, pos -> {

					int x = pos.x;
					int z = pos.z;

					x++;

					if(x > max.x)
					{
						x = min.x;
						z++;
					}

					if(z > max.z)
						throw new IllegalStateException("Stream limit didn't work.");

					return new ChunkPos(x, z);

				}).limit(diameter * diameter)
				.filter(c -> mc.world.isChunkLoaded(c.x, c.z))
				.map(c -> mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);

		return stream;
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

}
