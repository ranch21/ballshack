package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.render.Trajectories;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.ProjectileSim;

import java.util.Arrays;
import java.util.List;

public class ProjectileEvade extends Module {
	public ProjectileEvade() {
		super("ProjEvade", ModuleCategory.MOVEMENT, 0, new ModuleSettings(Arrays.asList(
				new SettingToggle(true, "Legit"),
				new SettingSlider(0.25, "HitboxExpand", 0, 1, 0.1)
		)), "endredman");
	}

	boolean prevInDanger = false;
	Vec3d fleeDir;

	@EventSubscribe
	public void onTick(EventTick event) {
		Trajectories trajModules = ((Trajectories) ModuleManager.getModuleByName("Trajectories"));
		trajModules.setTrajectories();
		List<ProjectileSim.Trajectory> trajectories = trajModules.getTrajectories();

		boolean inDanger = false;

		for (ProjectileSim.Trajectory traj : trajectories) {
			for (Vec3d pos : traj.getPositions()) {
				float width = traj.getProjectile().width;
				float height = traj.getProjectile().height;
				Box box = new Box(pos.x - width / 2, pos.y, pos.z - width / 2, pos.x + width / 2, pos.y + height, pos.z + width / 2);
				if (mc.player.getBoundingBox().expand((double) settings.getSetting(1).getValue()).intersects(box) && !traj.isFake()) {
					if (traj.getThrower() == null || traj.getThrower() != mc.player) {
						evade(traj);
						inDanger = true;
					}
				}
			}
		}

		prevInDanger = inDanger;

	}

	public void evade(ProjectileSim.Trajectory traj) {
		Vec3d lastPos = traj.getPositions().get(traj.getPositions().size() - 1);
		Vec3d almostLastPos = traj.getPositions().get(traj.getPositions().size() - 2);

		Vec3d vel = lastPos.subtract(almostLastPos);
		Vec3d d = almostLastPos.subtract(mc.player.getEntityPos());

		Vec3d a = new Vec3d(0, 1, 0);

		Vec3d perp = a.crossProduct(vel);

		boolean side = d.dotProduct(perp) > 0;

		perp = perp.normalize();

		if (side) {
			perp = perp.negate();
		}

		if ((boolean) settings.getSetting(0).getValue()) {
			fleeDir = perp;
			//PlayerUtil.setMovement(perp);
		} else {
			fleeDir = null;
			mc.player.setPosition(mc.player.getEntityPos().add(perp));
		}
	}

	@Override
	public void onDisable() {
		fleeDir = null;
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerMovementVector event) {
		if (fleeDir == null)
			return;
		PlayerUtil.setMovement(fleeDir, event);
	}
}
