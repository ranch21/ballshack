package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;

public class Debug extends Module {
	public Debug() {
		super("Debug", ModuleCategory.RENDER, 0, "yooohooo is this thing on?");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		MatrixStack matrices = event.matrixStack;

		Renderer renderer = Renderer.getInstance();

		//prevbox
		Vec3d ppos = new Vec3d(mc.player.lastX, mc.player.lastY, mc.player.lastZ);
		Box pbox = new Box(ppos.subtract(mc.player.getWidth() / 2, 0, mc.player.getWidth() / 2), ppos.add(mc.player.getWidth() / 2, mc.player.getHeight(), mc.player.getWidth() / 2));
		renderer.queueCubeOutline(pbox, Color.BLUE, matrices);
		//currbox
		renderer.queueCubeOutline(mc.player.getBoundingBox(), Color.GREEN, matrices);
		//lerpbox
		Vec3d lpos = mc.player.getLerpedPos(event.tickDelta);
		Box box = new Box(lpos.subtract(mc.player.getWidth() / 2, 0, mc.player.getWidth() / 2), lpos.add(mc.player.getWidth() / 2, mc.player.getHeight(), mc.player.getWidth() / 2));
		renderer.queueCubeOutline(box, Color.RED, matrices);

		//test shapes
		Vec3d offset1 = lpos.add(3, 0, 3);
		renderer.queueCube(new Box(offset1, offset1.add(1, 1, 1)), Colors.CLICKGUI_BACKGROUND_1.getColor(), matrices);
		renderer.queueCubeOutline(new Box(offset1, offset1.add(1, 1, 1)), Colors.PALETTE_1.getColor(), matrices);

		//stresstest
		Vec3d offset2 = lpos.add(3, 0, -3);
		for (int i = 0; i < 10000; i++) {
			renderer.queueCube(new Box(offset2.add((double) i / 100, i % 100, 0), offset2.add((double) i / 100 + 0.5, i % 100 + 0.5, 0.5)), Colors.CLICKGUI_BACKGROUND_1.getColor(), matrices);
		}
	}
}
