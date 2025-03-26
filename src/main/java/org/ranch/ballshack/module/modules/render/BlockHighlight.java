package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.lwjgl.opengl.GL11;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;
import java.util.Arrays;

public class BlockHighlight extends Module {
	public BlockHighlight() {
		super("BlockHighlight", ModuleCategory.RENDER, 0,  new ModuleSettings(Arrays.asList(
				new SettingSlider(0.5f, "Alpha", 0, 1, 0.1)
		)));
	}

	@EventSubscribe
	public void onOutlineRender(EventWorldRender.Outline event) {
		event.cancel();
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		Color c = Colors.PALLETE_1;

		double alpha = (double) getSettings().getSetting(0).getValue();
		float r = c.getRed() / 255.0f;
		float g = c.getGreen() / 255.0f;
		float b = c.getBlue() / 255.0f;

		if (mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
			if (blockHitResult.getType() != HitResult.Type.MISS) {
				BlockPos blockPos = blockHitResult.getBlockPos();
				BlockState blockState = mc.world.getBlockState(blockPos);
				if (!blockState.isAir() && mc.world.getWorldBorder().contains(blockPos)) {
					boolean bl = RenderLayers.getBlockLayer(blockState).isTranslucent();
					//if (bl != event.translucent) {
					//	return;
					//}

					MatrixStack matrices = event.matrixStack;

					matrices.push();
					Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
					matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

					VoxelShape shape =  blockState.getOutlineShape(mc.world, blockPos, ShapeContext.of(mc.gameRenderer.getCamera().getFocusedEntity()));

					for (Box box : shape.getBoundingBoxes()) {
						box = box.offset(blockPos);
						DrawUtil.drawCube(matrices, box, r, g,  b, (float) alpha);
						DrawUtil.drawCubeOutline(matrices, box, r, g, b, 1f);
					}

					matrices.pop();
				}
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);

	}
}
