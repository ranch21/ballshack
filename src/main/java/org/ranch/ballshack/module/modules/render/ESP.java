package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
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

public class ESP extends Module {

	public final NumberSetting alpha = dGroup.add(new NumberSetting("Alpha", 0.2).min(0).max(1).step(0.1));
	public final TargetsSettingGroup targets = addGroup(new TargetsSettingGroup("Targets"));

	public ESP() {
		super("ESP", ModuleCategory.RENDER, 0, "i see you");
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		Renderer renderer = Renderer.getInstance();

		MatrixStack matrices = event.matrixStack;

		for (Entity e : mc.world.getEntities()) {
			if (e != mc.player) {

				EntityUtil.EntityType type = EntityUtil.getEntityType(e);

				if (!targets.selected(type)) continue;

				Vec3d size = new Vec3d(e.getWidth(), e.getHeight(), e.getWidth());

				Vec3d c1 = e.getLerpedPos(event.tickDelta).subtract(size.x / 2, 0, size.z / 2);

				Box box = new Box(c1, c1.add(size));

				renderer.queueCube(box, BallColor.of(type.getColor()).setAlpha(alpha.getValueFloat()), matrices);
				renderer.queueCubeOutline(box, type.getColor(), matrices);
			}
		}
	}
}
