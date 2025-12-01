package org.ranch.ballshack.module.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;

import static org.ranch.ballshack.Constants.LINE_WIDTH;

public class Debug extends Module {
	public Debug() {
		super("Debug", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		MatrixStack matrices = event.matrixStack;

		Color color = Colors.PALLETE_1;

		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;
		float a = color.getAlpha() / 255.0f;

		Vec3d pos = mc.player.getLerpedPos(mc.getRenderTickCounter().getTickProgress(false));
		Vec3d vel = mc.player.getVelocity();

		Renderer renderer = Renderer.getInstance();

        /*renderer.renderCustom(((buffer, matrix) -> {
            buffer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z).color(r,g,b,a);
            buffer.vertex(matrix, (float) (pos.x + vel.x), (float) (pos.y + vel.y), (float) (pos.z + vel.z)).color(r,g,b,a);
        }), matrices, BallsRenderPipelines.LINE);*/
		//renderer.renderCube(mc.player.getBoundingBox(), Color.BLUE, matrices);

		//prevbox
		Vec3d ppos = new Vec3d(mc.player.lastX, mc.player.lastY, mc.player.lastZ);
		Box pbox = new Box(ppos.subtract(mc.player.getWidth() / 2, 0, mc.player.getWidth() / 2), ppos.add(mc.player.getWidth() / 2, mc.player.getHeight(), mc.player.getWidth() / 2));
		renderer.renderCubeOutlines(pbox, LINE_WIDTH, Color.BLUE, matrices);
		//currbox
		renderer.renderCubeOutlines(mc.player.getBoundingBox(), LINE_WIDTH, Color.GREEN, matrices);
		//lerpbox
		Vec3d lpos = mc.player.getLerpedPos(event.tickDelta);
		Box box = new Box(lpos.subtract(mc.player.getWidth() / 2, 0, mc.player.getWidth() / 2), lpos.add(mc.player.getWidth() / 2, mc.player.getHeight(), mc.player.getWidth() / 2));
		renderer.renderCubeOutlines(box, LINE_WIDTH, Color.RED, matrices);

		//test shapes
		Vec3d offset1 = lpos.add(3, 0, 3);
		renderer.renderCube(new Box(offset1, offset1.add(1, 1, 1)), Colors.DEFAULT_CLICKGUI_BACKGROUND, matrices);
		renderer.renderCubeOutlines(new Box(offset1, offset1.add(1, 1, 1)), LINE_WIDTH, Colors.PALLETE_1, matrices);

		//stresstest
		Vec3d offset2 = lpos.add(3, 0, -3);
		for (int i = 0; i < 10000; i++) {
			renderer.renderCube(new Box(offset2.add(i / 100, i % 100, 0), offset2.add(i / 100 + 0.5, i % 100 + 0.5, 0.5)), Colors.DEFAULT_CLICKGUI_BACKGROUND, matrices);
		}
		renderer.draw(BallsRenderPipelines.QUADS);

	}

}
