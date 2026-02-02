package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.Renderer;

public class Tracers extends Module {

	public final SettingSlider alpha = dGroup.add(new SettingSlider("Alpha", 0.4f, 0, 1, 0.1));
	public final TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));

	public Tracers() {
		super("Tracers", ModuleCategory.RENDER, 0, "i see you but with a line pointing at your feet... mmmgghg feeet");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		Renderer renderer = Renderer.getInstance();

		for (Entity e : mc.world.getEntities()) {
			if (e != mc.player) {

				EntityUtil.EntityType type = EntityUtil.getEntityType(e);

				if (!targets.selected(type)) continue;

				MatrixStack matrices = event.matrixStack;

				Vec3d pos = e.getLerpedPos(event.tickDelta);

				renderer.queueTracer(pos, BallColor.fromColor(type.getColor()).setAlpha(alpha.getValueFloat()), matrices);
			}
		}
	}
}
