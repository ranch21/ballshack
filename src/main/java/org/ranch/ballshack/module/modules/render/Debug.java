package org.ranch.ballshack.module.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

import java.awt.*;

public class Debug extends Module {
	public Debug() {
		super("Debug", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		MatrixStack matrices = event.matrixStack;

		matrices.push();

		Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

		matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		drawMovment(matrices);

		RenderSystem.setShaderColor(1, 1, 1, 1);

		matrices.pop();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public void drawMovment(MatrixStack matrices) {

		Color c = Colors.PALLETE_1;
		float r = c.getRed() / 255.0f;
		float g = c.getGreen() / 255.0f;
		float b = c.getBlue() / 255.0f;

		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		Vec3d pos = mc.player.getLerpedPos(mc.getRenderTickCounter().getTickDelta(false));
		//Vec2f movement = mc.player.input.getMovementInput();
		Vec3d vel = mc.player.getVelocity();

		bufferBuilder.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z);
		//bufferBuilder.vertex(matrix, (float) (pos.x + movement.x), (float) pos.y, (float) (pos.z + movement.y));

		//bufferBuilder.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z);
		bufferBuilder.vertex(matrix, (float) (pos.x + vel.x), (float) (pos.y + vel.y), (float) (pos.z + vel.z));

		RenderSystem.setShader(ShaderProgramKeys.POSITION);

		RenderSystem.setShaderColor(r, g, b, 1);

		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
	}
}
