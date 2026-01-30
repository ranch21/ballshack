package org.ranch.ballshack.debug.renderers;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.debug.DebugRenderer;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;

public class ScaffoldDebugRenderer extends DebugRenderer {

	BlockPos bpos;
	BlockHitResult hitResult;
	Direction bface;

	public void setData(BlockPos bpos, BlockHitResult hitResult, Direction bface) {
		this.bpos = bpos;
		this.hitResult = hitResult;
		this.bface = bface;
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		if (bpos == null || hitResult == null || bface == null) {
			return;
		}

		Renderer renderer = Renderer.getInstance();

		Vec3d faceCenter = bpos.toCenterPos().add(bface.getDoubleVector().multiply(0.5));

		renderer.renderArrow(faceCenter, faceCenter.add(bface.getDoubleVector().multiply(0.2)), 4, 0.2f, new Color(255, 0, 0), event.matrixStack);
		renderer.renderCube(new Box(bpos), new Color(0, 255, 0, 50), event.matrixStack);
		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
