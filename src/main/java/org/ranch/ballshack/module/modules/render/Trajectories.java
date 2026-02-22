package org.ranch.ballshack.module.modules.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.util.ProjectileSim;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Trajectories extends Module {
	private final List<ProjectileSim.Trajectory> trajectories = new ArrayList<>();

	public final BooleanSetting players = dGroup.add(new BooleanSetting("Players", true));
	public final NumberSetting alpha = dGroup.add(new NumberSetting("Alpha", 1).min(0).max(1).step(0.1));

	public Trajectories() {
		super("Trajectories", ModuleCategory.RENDER, 0, "i dont see you but now i see where my arrows are heading towards");
	}

	public List<ProjectileSim.Trajectory> getTrajectories() {
		return trajectories;
	}

	public void setTrajectories() {
		trajectories.clear();

		ProjectileEntity ent = ProjectileSim.prepareSim(mc.player);

		if (ent != null) {
			trajectories.add(ProjectileSim.simulate(ent, true, mc.player));
		}

		if (players.getValue()) {
			for (PlayerEntity player : mc.world.getPlayers()) {
				ProjectileEntity ent2 = ProjectileSim.prepareSim(player);

				if (ent != null) {
					trajectories.add(ProjectileSim.simulate(ent2, true, player));
				}
			}
		}

		for (Entity e : mc.world.getEntities()) {
			if (e instanceof ProjectileEntity) {
				trajectories.add(ProjectileSim.simulate((ProjectileEntity) e, false, null));
			}
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		setTrajectories();
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {

		MatrixStack matrices = event.matrixStack;

		Renderer renderer = Renderer.getInstance();

		for (ProjectileSim.Trajectory traj : trajectories) {

			Color c = Colors.SELECTED.getColor();

			Vec3d prevPos = null;

			for (Vec3d pos : traj.positions()) {

				if (prevPos == null) {
					prevPos = pos;
					continue;
				}

				if (pos == traj.positions().get(traj.positions().size() - 1)) {
					Box box = new Box(pos.subtract(0.1), pos.add(0.1));
					renderer.queueCube(box, BallColor.of(c).setAlpha(0.2f), matrices);
					renderer.queueCubeOutline(box, BallColor.of(c).setAlpha(0.7f), matrices);
				} else {
					renderer.queueLine(prevPos, pos, BallColor.of(c).setAlpha(alpha.getValueFloat()), matrices);
				}

				prevPos = pos;

			}
		}
	}
}
