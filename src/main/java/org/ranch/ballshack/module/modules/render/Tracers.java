package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.util.Arrays;

public class Tracers extends Module {
	public Tracers() {
		super("Tracers", ModuleCategory.RENDER, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(0.5f, "Alpha", 0, 1, 0.1),
				new TargetsDropDown("Targets")
		)), "i see you but with a line pointing at your feet... mmmgghg feeet");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		double alpha = (double) settings.getSetting(0).getValue();
		TargetsDropDown targets = (TargetsDropDown) settings.getSetting(1);

		Renderer renderer = Renderer.getInstance();

		for (Entity e : mc.world.getEntities()) {
			if (e != mc.player) {

				EntityUtil.EntityType type = EntityUtil.getEntityType(e);

				if (!targets.selected(type)) continue;

				MatrixStack matrices = event.matrixStack;

				Vec3d pos = e.getLerpedPos(event.tickDelta);

				Vec3d tracerStart = new Vec3d(0, 0, 200)
						.rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
						.rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
						.add(mc.gameRenderer.getCamera().getPos());

				renderer.renderLine(tracerStart, pos, 1, BallColor.fromColor(type.getColor()).setAlpha((float) alpha), matrices);
			}
		}

		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
