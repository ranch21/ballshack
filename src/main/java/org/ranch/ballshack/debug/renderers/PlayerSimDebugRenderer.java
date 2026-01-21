package org.ranch.ballshack.debug.renderers;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.util.PlayerSim;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.List;

public class PlayerSimDebugRenderer extends DebugRenderer {

	private List<PlayerSim.PlayerPoint> path;
	private Color color;

	public void setData(List<PlayerSim.PlayerPoint> path, Color color) {
		this.path = path;
		this.color = color;
	}

	@Override
	public void renderGui(DrawContext context) {

	}

	@Override
	public void render3d(Renderer context, MatrixStack matrixStack) {
		if (path == null)
			return;
		float size = 0.2f;
		for (PlayerSim.PlayerPoint pos : path) {
			context.renderCubeOutlines(pos.boundingBox(), 1, color, matrixStack);
		}
		context.draw(BallsRenderPipelines.QUADS);
	}
}
