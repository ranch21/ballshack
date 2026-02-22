package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.TargetsSettingGroup;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.ModeSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.setting.settings.SortModeSetting;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;
import org.ranch.ballshack.util.RotationUtil;

import java.util.ArrayList;
import java.util.List;

public class AimAssist extends Module {
	private static float pvel = 0.0f;
	private static float yvel = 0.0f;

	private static PerlinNoiseSampler noise;

	private static Vec3d prevTargetPos;
	private static Vec3d targetPos;
	private static Vec3d offset = Vec3d.ZERO;
	private static Vec3d prevOffset = Vec3d.ZERO;

	public static enum MoveMode {
		LINEAR,
		NATURAL,
		MOMENTUM
	}

	public final ModeSetting<MoveMode> mode = dGroup.add(new ModeSetting<>("Mode", MoveMode.LINEAR, MoveMode.values()).featured());
	public final NumberSetting range = dGroup.add(new NumberSetting("Range", 4).min(1).max(8).step(0.5));
	public final NumberSetting speed = dGroup.add(new NumberSetting("Speed", 8).min(1).max(25).step(1));

	public final ModuleSettingsGroup rGroup = addGroup(new ModuleSettingsGroup("Random Noise"));
	public final BooleanSetting rnEnabled = rGroup.add(new BooleanSetting("Enabled", true));
	public final NumberSetting rnAmount = rGroup.add(new NumberSetting("Amount", 0.4).min(0.1).max(1).step(0.1));
	public final NumberSetting rnSpeed = rGroup.add(new NumberSetting("Speed", 0.7).min(0.1).max(10).step(0.1));
	public final BooleanSetting rnSBR = rGroup.add(new BooleanSetting("SBR", false));
	public final NumberSetting rnSBRInfluence = rGroup.add(new NumberSetting("SBR Influence", 0.8).min(0.1).max(2).step(0.1));

	public final ModuleSettingsGroup tGroup = addGroup(new TargetsSettingGroup("Targets"));
	public final ModeSetting<SortModeSetting.SortMode> sortMode = dGroup.add(new SortModeSetting("Sort").featured());

	public AimAssist() {
		super("AimAssist", ModuleCategory.COMBAT, 0, "Controller player mode");
		noise = new PerlinNoiseSampler(Random.create());
	}

	@EventSubscribe
	public void onMouseUpdate(EventMouseUpdate event) {

		if (mc.player == null || mc.world == null)
			return;

		if (targetPos == null) return;

		double speed = this.speed.getValue();

		if (rnEnabled.getValue())
			speed *= (noise.sample(0.22, mc.world.getTime() * (rnSpeed.getValue() / 20), 0.74) / 2) + 1;

		Rotation desired = PlayerUtil.getPosRotation(mc.player, prevTargetPos.lerp(targetPos, event.timeDelta).add(prevOffset.lerp(offset, event.timeDelta)));

		float pdelta = RotationUtil.getDegreeChange(mc.player.lastPitch, desired.pitch);
		float ydelta = RotationUtil.getDegreeChange(mc.player.lastYaw, desired.yaw);

		if (mode.getValue() == MoveMode.LINEAR) {
			float combined = Math.max((ydelta + pdelta) / 25, 1);

			combined = combined * combined;

			speed *= combined * Math.abs(noise.sample(mc.world.getTime() / 1000.0f * event.timeDelta, 0.23, 0.11)) + 0.1;
			speed = Math.min(Math.max(speed, speed / 2f), 100);
		}

		Rotation step = RotationUtil.slowlyTurnTowards(desired, (float) speed);

		if (mode.getValue() == MoveMode.MOMENTUM) {
			yvel += step.yaw - mc.player.getYaw();
			pvel += step.pitch - mc.player.getPitch();

			event.deltaX = yvel + event.origDeltaX;
			event.deltaY = pvel + event.origDeltaY;

			if (pdelta + ydelta > 10) {
				yvel *= 0.97f;
				pvel *= 0.97f;
			} else {
				yvel *= 0.8f;
				pvel *= 0.8f;
			}
		} else {
			event.deltaX = (step.yaw - mc.player.getYaw()) + event.origDeltaX;
			event.deltaY = (step.pitch - mc.player.getPitch()) + event.origDeltaY;
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		double distance = range.getValue();

		boolean randomOffset = rnEnabled.getValue();
		double randAmount = rnAmount.getValue();
		double randSpeed = rnSpeed.getValue();
		boolean SBR = rnSBR.getValue();
		double SBRinf = rnSBRInfluence.getValue();

		//List<Entity> entities = EntityUtil.getEntities(distance, targetsDD, sortMode.getComparator(), true);
		List<Entity> entities = new ArrayList<>();


		if (entities.isEmpty()) {
			targetPos = null;
			return;
		}

		Entity e = entities.get(0);
		if (targetPos != null) {
			prevTargetPos = targetPos;
		}

		if (offset != null) {
			prevOffset = offset;
		}

		targetPos = EntityUtil.getCenter(e);

		if (randomOffset) {

			double time = mc.world.getTime() * (randSpeed / 100);
			offset = offset.add(
					noise.sample(time, 0.23, 0.11),
					noise.sample(0.86, time, 0.55),
					noise.sample(0.28, 0.58, time)
			);
			double mult = randAmount;
			if (SBR && prevTargetPos != null) {
				double entSpeed = targetPos.subtract(prevTargetPos).length();
				mult *= Math.max(0.1, entSpeed * SBRinf);
			}

			offset = offset.multiply(mult);
		}
	}
}
