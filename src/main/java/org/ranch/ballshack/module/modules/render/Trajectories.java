package org.ranch.ballshack.module.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.setting.settings.SettingToggle;
import org.ranch.ballshack.util.DrawUtil;
import org.ranch.ballshack.util.ProjectileSim;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Trajectories extends Module {
	private final List<Triple<List<Vec3d>, Entity, BlockPos>> trajectories = new ArrayList<>();

	public Trajectories() {
		super("Trajectories", ModuleCategory.RENDER, 0, new ModuleSettings(List.of(
				new SettingToggle(true, "Players"),
				new SettingSlider(1, "Alpha", 0, 1, 0.1)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		trajectories.clear();

		boolean players = (boolean) settings.getSetting(0).getValue();

		ProjectileEntity ent = ProjectileSim.prepareSim(mc.player);

		if (ent != null) {
			trajectories.add(ProjectileSim.simulate(ent));
		}

		if (players) {
			for (PlayerEntity player : mc.world.getPlayers()) {
				ProjectileEntity ent2 = ProjectileSim.prepareSim(player);

				if (ent != null) {
					trajectories.add(ProjectileSim.simulate(ent2));
				}
			}
		}

		for (Entity e : mc.world.getEntities()) {
			if (e instanceof ProjectileEntity) {
				trajectories.add(ProjectileSim.simulate((ProjectileEntity) e));
			}
		}

	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		double alpha = (double) getSettings().getSetting(1).getValue();

		MatrixStack matrices = event.matrixStack;

		matrices.push();

		Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		for (Triple<List<Vec3d>, Entity, BlockPos> traj : trajectories) {

			Color c = Colors.PALLETE_1;

			float r = c.getRed() / 255.0f;
			float g = c.getGreen() / 255.0f;
			float b = c.getBlue() / 255.0f;

			Vec3d prevPos = null;

			for (Vec3d pos : traj.getLeft()) {

				if (prevPos == null) {
					prevPos = pos;
					continue;
				}

				Matrix4f matrix = matrices.peek().getPositionMatrix();

				if (pos == traj.getLeft().get(traj.getLeft().size() - 1)) {
					Box box = new Box(pos.subtract(0.1), pos.add(0.1));
					DrawUtil.drawCube(matrices, box, r, g, b, 0.2f);
					DrawUtil.drawCubeOutline(matrices, box, r, g, b, 0.7f);
				} else {
					BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

					bufferBuilder.vertex(matrix, (float) prevPos.x, (float) prevPos.y, (float) prevPos.z);
					bufferBuilder.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z);

					RenderSystem.setShader(ShaderProgramKeys.POSITION);
					RenderSystem.setShaderColor(r, g, b, (float) alpha);
					BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
				}

				prevPos = pos;

			}
		}

		RenderSystem.setShaderColor(1, 1, 1, 1);

		matrices.pop();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
}
