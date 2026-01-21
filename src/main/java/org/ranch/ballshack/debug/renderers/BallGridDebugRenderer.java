package org.ranch.ballshack.debug.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.gui.balls.Ball;
import org.ranch.ballshack.gui.balls.BallHandler;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.List;

public class BallGridDebugRenderer extends DebugRenderer {

	private BallHandler ballHandler;
	private int width;
	private int height;

	public void setData(BallHandler ballHandler, int width, int height) {
		this.ballHandler = ballHandler;
		this.width = width;
		this.height = height;
	}

	@Override
	public void renderGui(DrawContext context) {
		context.drawText(MinecraftClient.getInstance().textRenderer, String.valueOf(ballHandler.frameTime), 5, 20, 0xFFFFFFFF, true);
		int gs = ballHandler.getGridSize();
		for (int i = 0; i < width / gs; i++) {
			for (int j = 0; j < height / gs; j++) {
				List<Ball> list = ballHandler.grid.get(ballHandler.hash(i, j));
				if (list != null) {
					context.fill(i * gs, j * gs, i * gs + gs, j * gs + gs, new Color(255, 0, 0, (int) MathHelper.clamp(list.size() * 200.0 / ((double) gs / Ball.size * (double) gs / Ball.size), 0, 255)).hashCode());
					context.drawText(MinecraftClient.getInstance().textRenderer, String.valueOf(list.size()), i * gs, j * gs, 0xFFFFFFFF, true);
				}
			}
		}
	}

	@Override
	public void render3d(Renderer context, MatrixStack matrixStack) {

	}
}
