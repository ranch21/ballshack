package org.ranch.ballshack.debug.renderers;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;

public class VecDebugRenderer extends DebugRenderer {

	private Vec3d vec;
	private Vec3d pos;
	private Color color;

	public void setData(Vec3d pos, Vec3d vec, Color color) {
		this.vec = vec;
		this.pos = pos;
		this.color = color;
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		Renderer renderer = Renderer.getInstance();

		renderer.renderArrow(pos, pos.add(vec), 4, 0.2f, color, event.matrixStack);
		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
