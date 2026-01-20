package org.ranch.ballshack.module.modules.world;

import net.minecraft.block.ShapeContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingMode;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.FreelookHandler;
import org.ranch.ballshack.util.PlayerSim;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.WorldUtil;

import java.util.*;

public class Scaffold extends Module {

	private class BlockScore {
		public int score;
		public BlockPlacement placement;

		public BlockScore(BlockPlacement placement, int score) {
			this.score = score;
			this.placement = placement;
		}
	}

	private class BlockPlacement {
		public BlockPos blockPos;
		public Direction bestDir;

		public BlockPlacement(BlockPos pos, Direction bestDir) {
			this.blockPos = pos;
			this.bestDir = bestDir;
		}
	}

	private static final Direction[] SEARCH_DIRECTIONS = {
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST,
			Direction.DOWN
	};

	private static final Direction[] ALL_DIRECTIONS = Direction.values();

	private double EPSILON = 0.0001;
	private int delay = 1;

	public SettingMode rotate = dGroup.add((SettingMode) new SettingMode(0, "Rotate", Arrays.asList("None", "Packet", "True")).featured());
	public SettingSlider delaySlider = dGroup.add(new SettingSlider(1, "Delay", 0, 10, 1));

	public Scaffold() {
		super("Scaffold", ModuleCategory.WORLD, 0);
	}

	@Override
	public void onEnable() {
		delay = (int) (double) delaySlider.getValue();
		super.onEnable();
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		int mode = rotate.getValue();
		int delayS = (int) (double) delaySlider.getValue();

		if (!mc.world.getBlockState(BlockPos.ofFloored(mc.player.getEntityPos().subtract(0, 0.1, 0))).isAir())
			return;

		Optional<BlockPlacement> winner = getBlock();

		if (winner.isEmpty())
			return;


		Vec3d faceMiddle = winner.get().blockPos.toCenterPos().add(winner.get().bestDir.getDoubleVector().multiply(0.5));

		FreelookHandler.enabled = false;

		switch (mode) {
			case 0:
				break;
			case 1:
				PlayerUtil.facePosPacket(faceMiddle);
				break;
			case 2:
				PlayerUtil.facePos(faceMiddle);
				FreelookHandler.enabled = true;
				break;
		}

		List<PlayerSim.PlayerPoint> future = PlayerSim.simulatePlayer(mc.player, 1);

		if (delay-- <= 0 || !future.get(0).onGround()) {
			WorldUtil.placeBlock(mc.player, Hand.MAIN_HAND, winner.get().blockPos, winner.get().bestDir);
			delay = delayS;
		}
	}

	public Optional<BlockPlacement> getBlock() {
		Vec3d ceilingPos = mc.player.getEntityPos().add(0, EPSILON, 0);
		// mmgggf feeet
		BlockPos atFeet = BlockPos.ofFloored(ceilingPos.subtract((0.1)));

		BlockPlacement winner = null;

		if (mc.player.supportingBlockPos.isPresent()) {
			BlockPos pos = mc.player.supportingBlockPos.get();
			Direction dir = whatGetsMeCloserToTheGoalStartingFromABlockPosWithAllDirectionsToChoose(pos, atFeet, ceilingPos.y);
			winner = new BlockPlacement(pos, dir);
		} else {
			List<BlockScore> candidates = new ArrayList<>();

			BlockPos.iterateRecursively(atFeet, 3, 100, (currentPos, queuer) -> {
				for (Direction direction : ALL_DIRECTIONS) {
					queuer.accept(currentPos.offset(direction));
				}
			}, (currentPos) -> {
				if (mc.world.getBlockState(currentPos).isAir())
					return BlockPos.IterationState.ACCEPT;

				int dist = (int) currentPos.toCenterPos().distanceTo(mc.player.getEntityPos()) * 10;
				Direction bestDir = whatGetsMeCloserToTheGoalStartingFromABlockPosWithAllDirectionsToChoose(currentPos, atFeet, ceilingPos.y);
				if (!sideVisible(currentPos, bestDir))
					return BlockPos.IterationState.ACCEPT;

				int yScore = currentPos.toBottomCenterPos().y + 1 > ceilingPos.y ? 100 : 0;
				candidates.add(new BlockScore(new BlockPlacement(currentPos, bestDir), dist + yScore));

				return BlockPos.IterationState.ACCEPT;
			});

			candidates.sort(Comparator.comparingInt(o -> o.score));

			if (!candidates.isEmpty()) {
				winner = candidates.get(0).placement;
			}
		}

		return Optional.ofNullable(winner);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		FreelookHandler.enabled = false;
	}

	public Direction whatGetsMeCloserToTheGoalStartingFromABlockPosWithAllDirectionsToChoose(BlockPos start, BlockPos goal, double ceiling) {
		if (start.toBottomCenterPos().y + 1 > ceiling)
			return Direction.DOWN;
		float best = Float.MAX_VALUE;
		Direction bestDir = Direction.UP;
		for (Direction direction : ALL_DIRECTIONS) {
			float dist = (float) start.offset(direction).toCenterPos().distanceTo(goal.toBottomCenterPos());
			if (dist < best) {
				best = dist;
				bestDir = direction;
			}
		}
		return bestDir;
	}

	public boolean sideVisible(BlockPos pos, Direction side) {
		Vec3d faceMiddle = pos.toCenterPos().add(side.getDoubleVector().multiply(0.5 + EPSILON));
		BlockHitResult hit = mc.world.raycast(new RaycastContext(mc.player.getEyePos(), faceMiddle, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, ShapeContext.absent()));
		return hit.getType() == HitResult.Type.MISS;
	}
}
