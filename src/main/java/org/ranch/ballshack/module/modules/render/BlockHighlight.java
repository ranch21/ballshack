package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;

import static org.ranch.ballshack.Constants.LINE_WIDTH;

public class BlockHighlight extends Module {

	public final SettingSlider alpha = dGroup.add(new SettingSlider("Alpha", 0.2f, 0, 1, 0.1));

	public BlockHighlight() {
		super("BlockHighlight", ModuleCategory.RENDER, 0, "look its purple now!");
	}

	@EventSubscribe
	public void onOutlineRender(EventWorldRender.Outline event) {
		event.cancel();
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		if (mc.world == null) // duh
			return;

		Color c = Colors.PALETTE_1.getColor();

		Renderer renderer = Renderer.getInstance();

		if (mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
			if (blockHitResult.getType() != HitResult.Type.MISS) {
				BlockPos blockPos = blockHitResult.getBlockPos();
				BlockState blockState = mc.world.getBlockState(blockPos);
				if (!blockState.isAir() && mc.world.getWorldBorder().contains(blockPos)) {

					MatrixStack matrices = event.matrixStack;

					VoxelShape shape = blockState.getOutlineShape(mc.world, blockPos, ShapeContext.of(mc.gameRenderer.getCamera().getFocusedEntity()));

					for (Box box : shape.getBoundingBoxes()) {
						box = box.offset(blockPos);
						renderer.renderCube(box, BallColor.fromColor(c).setAlpha((float) (double) alpha.getValue()), matrices);
						renderer.renderCubeOutlines(box, LINE_WIDTH, c, matrices);
					}
					renderer.draw(BallsRenderPipelines.QUADS);
				}
			}
		}
	}
}
