package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class HoleESP extends Module {

	public final SettingSlider alpha = dGroup.add(new SettingSlider("Alpha", 0.2f, 0, 1, 0.1));
	public final SettingSlider range = dGroup.add(new SettingSlider("Range", 5, 1, 10, 1));

	private final Direction[] directions = {
			Direction.DOWN,
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
	};

	private record Hole(BlockPos pos, float lowestResistance) {
	}

	public HoleESP() {
		super("HoleESP", ModuleCategory.RENDER, 0, "ihgfz");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		MatrixStack matrices = event.matrixStack;

		Renderer renderer = Renderer.getInstance();

		for (Hole hole : getHoles(range.getValueInt())) {
			Box box = Box.enclosing(hole.pos, hole.pos);
			box = box.withMaxY(box.minY + 0.1);

			Color c;

			if (hole.lowestResistance >= 3600000) {
				c = Color.GREEN;
			} else if (hole.lowestResistance >= 1200) {
				c = Color.YELLOW;
			} else {
				c = Color.RED;
			}

			renderer.queueCube(box, BallColor.fromColor(c).setAlpha(alpha.getValueFloat()), matrices);
			renderer.queueCubeOutline(box, BallColor.fromColor(c).setAlpha(0.7f), matrices);
		}
	}

	private Set<Hole> getHoles(int range) {
		Set<Hole> holes = new HashSet<>();
		for (int y = -range; y < range; y++) {
			for (int x = -range; x < range; x++) {
				next:
				for (int z = -range; z < range; z++) {
					BlockPos blockPos = BlockPos.ofFloored(mc.player.getEntityPos().add(x, y, z));
					BlockState blockState = mc.world.getBlockState(blockPos);
					if (!blockState.isAir() || !mc.world.getBlockState(blockPos.offset(Direction.UP)).isAir())
						continue;

					float lowest = Float.MAX_VALUE;
					for (Direction direction : directions) {
						BlockState side = mc.world.getBlockState(blockPos.offset(direction));
						if (side.isAir()) continue next;
						float resistance = side.getBlock().getBlastResistance();
						if (resistance < 9) continue next;
						lowest = Math.min(side.getBlock().getBlastResistance(), lowest);
					}
					holes.add(new Hole(blockPos, lowest));
				}
			}
		}
		return holes;
	}
}
