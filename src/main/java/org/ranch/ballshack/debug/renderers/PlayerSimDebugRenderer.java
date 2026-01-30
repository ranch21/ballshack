package org.ranch.ballshack.debug.renderers;

import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
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

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		Renderer renderer = Renderer.getInstance();
		if (path == null)
			return;
		float size = 0.2f;
		for (PlayerSim.PlayerPoint pos : path) {
			renderer.renderCubeOutlines(pos.boundingBox(), 1, color, event.matrixStack);
		}
		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
