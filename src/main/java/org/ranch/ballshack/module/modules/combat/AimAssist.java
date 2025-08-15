package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;
import org.ranch.ballshack.util.RotationUtil;

import java.util.Arrays;
import java.util.List;

public class AimAssist extends Module {
	private static float pvel = 0.0f;
	private static float yvel = 0.0f;

	private static PerlinNoiseSampler noise;

	private static Vec3d prevTargetPos;
	private static Vec3d targetPos;

	public AimAssist() {
		super("AimAssist", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingMode(0, "Mode", Arrays.asList("Linear", "\"natural\"", "Momentum")).featured(),
				new SettingSlider(4, "Range", 1, 8, 0.5),
				new SettingSlider(8, "Speed", 1, 25, 1),
				new DropDown("Random Noise", Arrays.asList(
						new SettingToggle(true, "Enabled"),
						new SettingSlider(0.4, "Amount", 0.1, 1, 0.1),
						new SettingSlider(0.7, "Speed", 0.1, 10, 0.1),
						new SettingToggle(false, "SBR"),
						new SettingSlider(0.8, "SBR Influence", 0.1, 2, 0.1)
				)),
				new TargetsDropDown("Targets"),
				new SortMode("Sort").featured()
		)), "Controller player mode");
		noise = new PerlinNoiseSampler(Random.create());
	}

	@EventSubscribe
	public void onMouseUpdate(EventMouseUpdate event) {

		if (targetPos == null) return;

		int moveMode = (int) getSettings().getSetting(0).getValue();

		double speed = (double) getSettings().getSetting(2).getValue();

		DropDown randomSettings = (DropDown) getSettings().getSetting(3);
		boolean randomOffset = (boolean) randomSettings.getSetting(0).getValue();
		double randAmount = (double) randomSettings.getSetting(1).getValue();
		double randSpeed = (double) randomSettings.getSetting(2).getValue();
		boolean SBR = (boolean) randomSettings.getSetting(3).getValue();
		double SBRinf = (double) randomSettings.getSetting(4).getValue();

		Vec3d offset = Vec3d.ZERO;

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
				mult *= entSpeed * SBRinf;
			}

			BallsLogger.info(mult);
			offset = offset.multiply(mult);
		}

		Rotation desired = PlayerUtil.getPosRotation(mc.player, targetPos.add(offset));

		float pdelta = RotationUtil.getDegreeChange(mc.player.prevPitch,desired.pitch);
		float ydelta = RotationUtil.getDegreeChange(mc.player.prevYaw,desired.yaw);

		if (moveMode == 1) {
			float combined = Math.max((ydelta + pdelta) / 25, 1);

			combined = Math.min(combined * combined, 30);

			speed *= combined;
		}

		Rotation step = RotationUtil.slowlyTurnTowards(desired, (float) speed);

		if (moveMode == 2) {
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
		double distance = (double) getSettings().getSetting(1).getValue();

		TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(4);

		SortMode mode = (SortMode) getSettings().getSetting(5);

		List<Entity> entities = EntityUtil.getEntities(distance, targets, mode.getComparator());

		if (entities.isEmpty()) {
			targetPos = null;
			return;
		}

		Entity e = entities.get(0);
		if (targetPos != null) {
			prevTargetPos = targetPos;
		}

		targetPos = EntityUtil.getCenter(e);
	}
}
