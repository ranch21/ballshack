package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import static org.ranch.ballshack.Constants.LINE_WIDTH;

public class ESP extends Module {

	public final SettingSlider alpha = dGroup.add(new SettingSlider(0.2f, "Alpha", 0, 1, 0.1));
	public final TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));

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

				renderer.renderCube(box, BallColor.fromColor(type.getColor()).setAlpha((float) (double) alpha.getValue()), matrices);
				renderer.renderCubeOutlines(box, LINE_WIDTH, type.getColor(), matrices);
			}
		}

		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
