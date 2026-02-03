package org.ranch.ballshack.module.modules.combat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.modules.render.HoleESP;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrystalAura extends Module {

	public final SettingSlider range = dGroup.add(new SettingSlider("Range", 4, 1, 8, 0.5));

	public final TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));
	public final SettingMode rotate = dGroup.add(new SettingMode("Rotate", 0, Arrays.asList("None", "Packet", "True")).featured());
	public final SettingToggle freeLook = dGroup.add(new SettingToggle("FreeLook", true).depends(() -> rotate.getValue() == 2));
	public final SettingToggle swing = dGroup.add(new SettingToggle("Swing", true));
	public final SortMode sort = dGroup.add(new SortMode("Sort"));

	public CrystalAura() {
		super("CrystalAura", ModuleCategory.COMBAT, 0, "boomboomboom");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		double distance = range.getValue();

		FreelookHandler.disable();

		FreelookHandler.setEnabled(freeLook.getValue() && freeLook.dependencyMet());

		Set<BlockPos> supports = getCrystalSupports(range.getValueInt() + 1);

		if (supports.isEmpty())
			return;

		for (Entity e : EntityUtil.getEntities(distance, targets, sort.getComparator())) {

			BlockPos winner = evaluateSupports(supports, e);
			Vec3d faceMiddle = winner.toCenterPos().add(Direction.UP.getDoubleVector().multiply(0.5));

			switch (rotate.getValue()) {
				case 0:
					break;
				case 1:
					PlayerUtil.facePosPacket(faceMiddle);
					break;
				case 2:
					PlayerUtil.facePos(faceMiddle);
					break;
			}

			WorldUtil.placeBlock(mc.player.getActiveHand(), winner, Direction.UP);

			break;
		}
	}

	public Set<BlockPos> getCrystalSupports(int range) {
		Set<BlockPos> supports = new HashSet<>();
		for (int y = -range; y < range; y++) {
			for (int x = -range; x < range; x++) {
				for (int z = -range; z < range; z++) {
					BlockPos blockPos = BlockPos.ofFloored(mc.player.getEntityPos().add(x, y, z));
					BlockState blockState = mc.world.getBlockState(blockPos);
					if (blockState.isAir() || !mc.world.getBlockState(blockPos.offset(Direction.UP)).isAir())
						continue;

					if (blockState.getBlock() == Blocks.OBSIDIAN || blockState.getBlock() == Blocks.BEDROCK)
						supports.add(blockPos);
				}
			}
		}
		return supports;
	}

	public BlockPos evaluateSupports(Set<BlockPos> supports, Entity target) {
		return supports.stream().toList().get(0);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		FreelookHandler.disable();
	}
}
