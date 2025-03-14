package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.setting.settings.TargetsDropDown;
import org.ranch.ballshack.util.DrawUtil;
import org.ranch.ballshack.util.EntityUtil;

import java.awt.*;
import java.util.Arrays;

public class ESP extends Module {
	public ESP() {
		super("ESP", ModuleCategory.RENDER, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(0.5f, "Alpha", 0, 1, 0.1),
				new TargetsDropDown("Targets")
		)));
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		double alpha = (double) getSettings().getSetting(0).getValue();
		TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(1);



		for (Entity e : mc.world.getEntities()) {
			if (e != mc.player) {

				Color c = DrawUtil.getEspColor(e);

				if (EntityUtil.isAnimal(e)) {
					if (!targets.getPassive()) continue;
				} else if (EntityUtil.isMob(e)) {
					if (!targets.getMobs()) continue;
				} else if (EntityUtil.isPlayer(e)) {
					if (!targets.getPlayers()) continue;
				} else {
					continue;
				}

				MatrixStack matrices = event.matrixStack;

				Vec3d size = new Vec3d(e.getWidth(), e.getHeight(), e.getWidth());

				Vec3d c1 = e.getLerpedPos(event.tickDelta).subtract(size.x / 2, 0, size.z / 2);

				Box box = new Box(c1, c1.add(size));

				float r = c.getRed() / 255.0f;
				float g = c.getGreen() / 255.0f;
				float b = c.getBlue() / 255.0f;

				matrices.push();
				Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
				matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

				DrawUtil.drawCube(matrices, box, r, g, b, (float) alpha);
				DrawUtil.drawCubeOutline(matrices, box, r, g, b, 0.7f);

				matrices.pop();
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);

	}
}
