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
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingMode;
import org.ranch.ballshack.util.FreelookHandler;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.WorldUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Scaffold extends Module {

    private class BlockScore {
        public int score;
        public BlockPos blockPos;
        public Direction bestDir;

        public BlockScore(BlockPos pos, int score, Direction bestDir) {
            this.score = score;
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
    private boolean sneaking = false;

    public Scaffold() {
        super("Scaffold", ModuleCategory.WORLD, 0, new ModuleSettings(Arrays.asList(
                new SettingMode(0, "Rotate", Arrays.asList("None", "Packet", "True")).featured()
                )));
    }

    @EventSubscribe
    public void onTick(EventTick event) {
        // mmgggf feeet
        if (mc.player.isSneaking())
            return;

        Vec3d ceilingPos = mc.player.getEntityPos().add(0, EPSILON, 0);
        BlockPos atFeet = BlockPos.ofFloored(ceilingPos.subtract((0.1)));

        int mode = (int) getSettings().getSetting(0).getValue();

        List<BlockScore> candidates = new ArrayList<>();

        if (!mc.world.getBlockState(BlockPos.ofFloored(mc.player.getEntityPos().subtract(0, 0.1, 0))).isAir())
            return;

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
            candidates.add(new BlockScore(currentPos, dist + yScore, bestDir));

            return BlockPos.IterationState.ACCEPT;
        });

        if (candidates.isEmpty())
            return;

        candidates.sort(Comparator.comparingInt(o -> o.score));

        Vec3d faceMiddle = candidates.get(0).blockPos.toCenterPos().add(candidates.get(0).bestDir.getDoubleVector().multiply(0.5));

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

        WorldUtil.placeBlock(mc.player, Hand.MAIN_HAND, candidates.get(0).blockPos, candidates.get(0).bestDir);
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
