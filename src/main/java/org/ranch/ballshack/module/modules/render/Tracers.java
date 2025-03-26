package org.ranch.ballshack.module.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.DrawUtil;
import org.ranch.ballshack.util.EntityUtil;

import java.awt.*;
import java.util.Arrays;

public class Tracers extends Module {
	public Tracers() {
		super("Tracers", ModuleCategory.RENDER, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(0.5f, "Alpha", 0, 1, 0.1),
				new TargetsDropDown("Targets")
		)));
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		double alpha = (double) settings.getSetting(0).getValue();
		TargetsDropDown targets = (TargetsDropDown) settings.getSetting(1);

		for (Entity e : mc.world.getEntities()) {
			if (e != mc.player) {

				Color c = DrawUtil.getEspColor(e);

				if (EntityUtil.isAnimal(e)) {
					if (!targets.getPassive()) continue;
				} else if (EntityUtil.isMob(e)) {
					if (!targets.getMobs()) continue;
				} else if (EntityUtil.isPlayer(e)) {
					if (!targets.getPlayers()) continue;
				} else {
					continue;
				}

				MatrixStack matrices = event.matrixStack;

				Vec3d pos = e.getLerpedPos(event.tickDelta);

				float r = c.getRed() / 255.0f;
				float g = c.getGreen() / 255.0f;
				float b = c.getBlue() / 255.0f;

				matrices.push();

				Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

				Vec3d tracerStart = new Vec3d(0, 0, 200)
						.rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
						.rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
						.add(mc.gameRenderer.getCamera().getPos());

				matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

				BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

				Matrix4f matrix = matrices.peek().getPositionMatrix();

				bufferBuilder.vertex(matrix, (float) tracerStart.x, (float) tracerStart.y, (float) tracerStart.z);
				bufferBuilder.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z);

				RenderSystem.setShader(ShaderProgramKeys.POSITION);

				RenderSystem.setShaderColor(r,g,b, (float) alpha);

				BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

				RenderSystem.setShaderColor(1,1,1,1);

				matrices.pop();
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);

	}
}
