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
import org.ranch.ballshack.setting.moduleSettings.SettingMode;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.ProjectileSim;

import java.util.Arrays;
import java.util.List;

public class ProjectileEvade extends Module {

	public final SettingMode mode = dGroup.add(new SettingMode(0, "Mode", Arrays.asList("Legit", "LegitSprint", "TP")));
	public final SettingSlider hExpand = dGroup.add(new SettingSlider(0.25, "HitboxExpand", 0, 1, 0.1));

	public ProjectileEvade() {
		super("ProjEvade", ModuleCategory.MOVEMENT, 0, "endredman");
	}

	//todo fix this module

	boolean prevInDanger = false;
	Vec3d fleeDir;

	@EventSubscribe
	public void onTick(EventTick event) {
		Trajectories trajModules = ((Trajectories) ModuleManager.getModuleByName("Trajectories"));
		trajModules.setTrajectories();
		List<ProjectileSim.Trajectory> trajectories = trajModules.getTrajectories();

		boolean inDanger = false;

		for (ProjectileSim.Trajectory traj : trajectories) {
			int i = 0;
			for (Vec3d pos : traj.positions()) {
				float width = traj.projectile().width;
				float height = traj.projectile().height;
				Box box = new Box(pos.x - width / 2, pos.y, pos.z - width / 2, pos.x + width / 2, pos.y + height, pos.z + width / 2);
				if (mc.player.getBoundingBox().expand(hExpand.getValue()).intersects(box) && !traj.fake()) {
					if (traj.thrower() == null || traj.thrower() != mc.player) {
						evade(traj, i++);
						inDanger = true;
					}
				}
			}
		}

		if (!inDanger && prevInDanger) {
			fleeDir = null;
		}

		prevInDanger = inDanger;

	}

	public void evade(ProjectileSim.Trajectory traj, int i) {

		Vec3d lastPos = traj.positions().get(traj.positions().size() - 1);
		Vec3d almostLastPos = traj.positions().get(0);

		Vec3d vel = lastPos.subtract(almostLastPos);
		Vec3d d = almostLastPos.subtract(mc.player.getEntityPos());

		Vec3d a = new Vec3d(0, 1, 0);

		Vec3d perp = a.crossProduct(vel);

		boolean side = d.dotProduct(perp) > 0;

		perp = perp.normalize();

		if (side) {
			perp = perp.negate();
		}

		switch (mode.getValue()) {
			case 0:
				fleeDir = perp;
				break;
			case 1:
				//RotationUtil.slowlyTurnTowards(new Rotation(perp), 100);
				mc.player.setYaw((float) Math.toDegrees(Math.atan2(perp.z, perp.x) - 90f));
				mc.options.forwardKey.setPressed(true);
				mc.player.setSprinting(
						mc.player.input.getMovementInput().x > 0 && !mc.player.isSneaking() && mc.player.getHungerManager().getFoodLevel() >= 6);
				break;
			case 2:
				mc.player.setPosition(mc.player.getEntityPos().add(perp));
		}
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerMovementVector event) {
		if (mode.getValue() < 2 && fleeDir != null) {
			PlayerUtil.setMovement(fleeDir, event);
		}
	}

	@Override
	public void onDisable() {
		fleeDir = null;
	}
}
