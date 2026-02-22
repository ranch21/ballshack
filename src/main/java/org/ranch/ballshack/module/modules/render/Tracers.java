package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.TargetsSettingGroup;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.Renderer;

public class Tracers extends Module {

	public final NumberSetting alpha = dGroup.add(new NumberSetting("Alpha", 0.4).min(0).max(1).step(0.1));
	public final TargetsSettingGroup targets = addGroup(new TargetsSettingGroup("Targets"));

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

				renderer.queueTracer(pos, BallColor.of(type.getColor()).setAlpha(alpha.getValueFloat()), matrices);
			}
		}
	}
}
