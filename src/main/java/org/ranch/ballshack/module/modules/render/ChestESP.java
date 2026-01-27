package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.WorldUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.stream.Stream;

import static org.ranch.ballshack.Constants.LINE_WIDTH;

public class ChestESP extends Module {

	public final SettingSlider alpha = dGroup.add(new SettingSlider(0.2f, "Alpha", 0, 1, 0.1));

	public ChestESP() {
		super("ChestESP", ModuleCategory.RENDER, 0, "\"i found a spawner\"");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		if (mc.world == null)
			return;

		Stream<BlockEntity> blockEntities = WorldUtil.getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream());

		Renderer renderer = Renderer.getInstance();
		MatrixStack matrices = event.matrixStack;

		for (BlockEntity bEnt : blockEntities.toList()) {

			WorldUtil.InvType type = WorldUtil.getInvType(bEnt);

			if (type == null) continue;

			Color c = Colors.INVENTORY_COLORS[type.ordinal()];

			BlockPos blockPos = bEnt.getPos();

			BlockState blockState = mc.world.getBlockState(blockPos);

			VoxelShape shape = blockState.getOutlineShape(mc.world, blockPos, ShapeContext.of(mc.gameRenderer.getCamera().getFocusedEntity()));

			float r = c.getRed() / 255.0f;
			float g = c.getGreen() / 255.0f;
			float b = c.getBlue() / 255.0f;

			for (Box box : shape.getBoundingBoxes()) {
				box = box.offset(blockPos);
				renderer.renderCube(box, BallColor.fromColor(c).setAlpha((float) (double) alpha.getValue()), matrices);
				renderer.renderCubeOutlines(box, LINE_WIDTH, BallColor.fromColor(c).setAlpha((float) (double) alpha.getValue()), matrices);
			}

		}

		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
