package org.ranch.ballshack.module.modules.world;

import net.minecraft.block.ShapeContext;
import net.minecraft.item.BlockItem;
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
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.ModeSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.setting.settings.RotateModeSetting;
import org.ranch.ballshack.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Scaffold extends Module {

	public record BlockScore(BlockPlacement placement, int score) {
	}

	public record BlockPlacement(BlockPos blockPos, Direction bestDir) {
	}

	private static final Direction[] ALL_DIRECTIONS = Direction.values();

	private final double EPSILON = 0.001;
	private int delay = 1;

	public final ModeSetting<RotateModeSetting.RotateMode> rotate = dGroup.add(new RotateModeSetting("Rotate").featured());
	public final BooleanSetting freeLook = dGroup.add(new BooleanSetting("FreeLook", true).depends(() -> rotate.getValue() == RotateModeSetting.RotateMode.TRUE));
	public final BooleanSetting slowRotate = dGroup.add(new BooleanSetting("SlowRotate", true).depends(() -> rotate.getValue() == RotateModeSetting.RotateMode.TRUE));
	public final NumberSetting rotSpeed = dGroup.add(new NumberSetting("RotateSpeed", 5).min(2).max(40).step(1).depends(slowRotate::getValue));
	public final NumberSetting delaySlider = dGroup.add(new NumberSetting("Delay", 1).min(0).max(10).step(1));

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
		if (!(mc.player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BlockItem))
			return;

		int delayS = (int) (double) delaySlider.getValue();

		List<PlayerSim.PlayerPoint> future = PlayerSim.simulatePlayer(mc.player, 10);
		int firstAirIndex = PlayerSim.getFirst(future, (playerPoint -> !playerPoint.onGround()));
		firstAirIndex = Math.max(0, firstAirIndex - 1);
		PlayerSim.PlayerPoint firstAir = future.get(firstAirIndex);

		Optional<BlockPlacement> currentWinner = getBlock(mc.player.getEntityPos(), mc.player.getEyePos(), mc.player.supportingBlockPos);
		Optional<BlockPlacement> futureWinner = getBlock(firstAir.position(), firstAir.eyePos(), firstAir.supportingBlock());

		Optional<BlockPlacement> winner;

		if (futureWinner.isPresent() && firstAirIndex != future.size() - 1) {
			winner = futureWinner;
		} else if (currentWinner.isPresent()) {
			winner = currentWinner;
		} else {
			return;
		}

		FreelookHandler.setEnabled(freeLook.getValue() && freeLook.dependencyMet());

		Vec3d faceMiddle = winner.get().blockPos.toCenterPos().add(winner.get().bestDir.getDoubleVector().multiply(0.5));
		Vec3d faceMiddleAnticipated = faceMiddle.subtract(firstAir.position().subtract(mc.player.getEntityPos()));

		boolean canPlace = false;

		switch (rotate.getValue()) {
			case NONE:
				canPlace = true;
				break;
			case PACKET:
				canPlace = true;
				PlayerUtil.facePosPacket(faceMiddleAnticipated);
				break;
			case TRUE:
				if (slowRotate.getValue()) {
					Rotation target = PlayerUtil.getPosRotation(mc.player, faceMiddleAnticipated);
					Rotation step = RotationUtil.slowlyTurnTowards(target, rotSpeed.getValueFloat());
					mc.player.setYaw((step.yaw - mc.player.getYaw()) + mc.player.getYaw());
					mc.player.setPitch((step.pitch - mc.player.getPitch()) + mc.player.getPitch());
					if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
						BlockHitResult result = (BlockHitResult) mc.crosshairTarget;
						canPlace = result.getBlockPos().equals(winner.get().blockPos) && result.getSide() == winner.get().bestDir;
					}
				} else {
					canPlace = true;
					PlayerUtil.facePos(faceMiddleAnticipated);
				}
				break;
		}

		if ((delay-- <= 0 || !future.get(0).onGround()) && canPlace && sideVisible(winner.get().blockPos, winner.get().bestDir, mc.player.getEyePos())) {
			WorldUtil.placeBlock(Hand.MAIN_HAND, winner.get().blockPos, winner.get().bestDir);
			delay = delayS;
		}
	}

	public Optional<BlockPlacement> getBlock(Vec3d playerPos, Vec3d eyePos, Optional<BlockPos> supportingBlock) {
		Vec3d ceilingPos = playerPos.add(0, EPSILON, 0);
		// mmgggf feeet
		BlockPos atFeet = BlockPos.ofFloored(ceilingPos.subtract((0.1)));

		BlockPlacement winner = null;

		if (supportingBlock.isPresent()) {
			BlockPos pos = supportingBlock.get();
			Direction dir = whatGetsMeCloserToTheGoalStartingFromABlockPosWithAllDirectionsToChoose(pos, atFeet, ceilingPos.y);
			if (sideVisible(pos, dir, eyePos))
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

				int dist = (int) currentPos.toCenterPos().distanceTo(playerPos) * 10;
				Direction bestDir = whatGetsMeCloserToTheGoalStartingFromABlockPosWithAllDirectionsToChoose(currentPos, atFeet, ceilingPos.y);
				if (!sideVisible(currentPos, bestDir, eyePos))
					return BlockPos.IterationState.ACCEPT;

				int yScore = currentPos.toBottomCenterPos().y > ceilingPos.y ? 100 : 0;
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
		FreelookHandler.disable();
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

	public boolean sideVisible(BlockPos pos, Direction side, Vec3d start) {
		Vec3d faceMiddle = pos.toCenterPos().add(side.getDoubleVector().multiply(0.5));
		BlockHitResult hit = mc.world.raycast(new RaycastContext(start, faceMiddle, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, ShapeContext.absent()));
		return hit.getType() == HitResult.Type.MISS;
	}
}
