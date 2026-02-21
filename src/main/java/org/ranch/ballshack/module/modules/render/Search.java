package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.chunk.WorldChunk;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacket;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.settings.BlocksSetting;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.util.WorldUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Search extends Module {

	private final Set<BlockPos> foundBlocks = ConcurrentHashMap.newKeySet();
	private final Set<ChunkPos> queuedChunks = ConcurrentHashMap.newKeySet();

	private final ExecutorService executorService = Executors.newFixedThreadPool(2);

	public final NumberSetting alpha = dGroup.add(new NumberSetting("Alpha", 0.2f).min(0).max(1).step(0.1));
	public final BooleanSetting tracers = dGroup.add(new BooleanSetting("Tracers", true));
	public final BlocksSetting blocks = dGroup.add(new BlocksSetting("Blocks"));


	public Search() {
		super("Search", ModuleCategory.RENDER, 0, "wya nether portal (use .search)");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		if (mc.world == null || foundBlocks.isEmpty())
			return;

		MatrixStack matrices = event.matrixStack;
		Renderer renderer = Renderer.getInstance();


		for (BlockPos foundBlock : foundBlocks) {
			BlockState foundState = mc.world.getBlockState(foundBlock);
			if (foundState.isAir()) {
				foundBlocks.remove(foundBlock);
				continue;
			}

			VoxelShape shape = foundState.getOutlineShape(mc.world, foundBlock, ShapeContext.of(mc.gameRenderer.getCamera().getFocusedEntity()));

			Color color = getBlockColor(foundState);

			for (Box box : shape.getBoundingBoxes()) {
				box = box.offset(foundBlock);
				renderer.queueCube(box, BallColor.fromColor(color).setAlpha(alpha.getValueFloat()), matrices);
				renderer.queueCubeOutline(box, color, matrices);
				if (tracers.getValue())
					renderer.queueTracer(box.getCenter(), BallColor.fromColor(color).setAlpha(0.7f), matrices);
			}
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		if (mc.world == null || queuedChunks.isEmpty())
			return;

		List<ChunkPos> sortedList = queuedChunks.stream().sorted(Comparator.comparingDouble(this::getChunkDistance)).toList();
		for (ChunkPos cpos : sortedList) {
			queuedChunks.remove(cpos);
			WorldChunk chunk = mc.world.getChunk(cpos.x, cpos.z);
			executorService.submit(() -> {
				for (int y = mc.world.getBottomY(); y < mc.world.getTopYInclusive(); y++) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							BlockPos pos = new BlockPos(x + cpos.getStartX(), y, z + cpos.getStartZ());
							BlockState state = chunk.getBlockState(pos);
							if (blocks.getBlocks().contains(state.getBlock())) {
								foundBlocks.add(pos.toImmutable());
							}
						}
					}
				}
			});
		}
	}

	public void reload() {
		if (mc.world == null)
			return;

		queuedChunks.clear();
		foundBlocks.clear();
		clearUnloaded();

		WorldUtil.getLoadedChunks().forEach((worldChunk -> {
			queuedChunks.add(worldChunk.getPos());
		}));
	}

	public void clearUnloaded() {
		if (mc.world == null)
			return;

		queuedChunks.removeIf(cpos -> !mc.world.isChunkLoaded(cpos.x, cpos.z));
	}

	@EventSubscribe
	public void onPacket(EventPacket.Receive event) {
		if (event.packet instanceof ChunkDataS2CPacket chunkDataS2CPacket) {
			ChunkPos cpos = new ChunkPos(chunkDataS2CPacket.getChunkX(), chunkDataS2CPacket.getChunkZ());
			queuedChunks.add(cpos);
		}

		if (event.packet instanceof ChunkDeltaUpdateS2CPacket chunkDeltaUpdateS2CPacket) {
			chunkDeltaUpdateS2CPacket.visitUpdates((bpos, bstate) -> {
				queuedChunks.add(mc.world.getChunk(bpos).getPos());
			});
		}

		if (event.packet instanceof UnloadChunkS2CPacket unloadChunkS2CPacket) {
			queuedChunks.remove(unloadChunkS2CPacket.pos());
		}

		if (event.packet instanceof BlockUpdateS2CPacket blockUpdateS2CPacket) {
			ChunkPos cpos = mc.world.getChunk(blockUpdateS2CPacket.getPos()).getPos();
			queuedChunks.add(cpos);
		}

		if (event.packet instanceof DisconnectS2CPacket || event.packet instanceof GameJoinS2CPacket) {
			reload();
		}
	}

	private double getChunkDistance(ChunkPos chunkPos) {
		double cx = chunkPos.x * 16;
		double cz = chunkPos.z * 16;
		double px = mc.player.getX();
		double pz = mc.player.getZ();
		return ((px - cx) * (px - cx)) + ((pz - cz) * (pz - cz));
	}

	private Color getBlockColor(BlockState state) {
		Block block = state.getBlock();

		if (block == Blocks.NETHER_PORTAL) {
			return Color.MAGENTA;
		} else if (block == Blocks.BARRIER) {
			return Color.RED;
		} else {
			return new Color(state.getMapColor(mc.world, new BlockPos(0, 10000, 0)).color);
		}
	}
}
