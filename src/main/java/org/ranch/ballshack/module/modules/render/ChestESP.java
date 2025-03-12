package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.util.DrawUtil;
import org.ranch.ballshack.util.WorldUtil;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

public class ChestESP extends Module {
	public ChestESP() {
		super("ChestESP", ModuleCategory.RENDER, 0, new ModuleSettings(List.of(
				new SettingSlider(0.5f, "Alpha", 0, 1, 0.1)
		)));
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender event) {

		Stream<BlockEntity> blockEntities = WorldUtil.getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		double alpha = (double) getSettings().getSetting(0).getValue();

		for (BlockEntity bEnt : blockEntities.toList()) {

			WorldUtil.InvType type = WorldUtil.getInvType(bEnt);

			if (type == null) continue;

			Color c = Colors.INVENTORY_COLORS[type.ordinal()];

			MatrixStack matrices = event.matrixStack;

			Vec3d size = new Vec3d(1,1,1);

			BlockPos blockPos = bEnt.getPos();

			Vec3d pos = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());

			Box box = new Box(pos, pos.add(size));

			float r = c.getRed() / 255.0f;
			float g = c.getGreen() / 255.0f;
			float b = c.getBlue() / 255.0f;

			matrices.push();
			Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
			matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

			DrawUtil.drawCube(matrices, box, r, g, b, (float) alpha);
			DrawUtil.drawCubeOutline(matrices, box, r, g, b, 0.7f);

			matrices.pop();
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);

	}
}
