package org.ranch.ballshack.module.modules.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;
import org.ranch.ballshack.util.RotationUtil;
import org.ranch.ballshack.util.rendering.BallsRenderPipelines;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AimAssist extends Module {
	private static float pvel = 0.0f;
	private static float yvel = 0.0f;

	private static PerlinNoiseSampler noise;

	private static Vec3d prevTargetPos;
	private static Vec3d targetPos;
	private static Vec3d offset = Vec3d.ZERO;
	private static Vec3d prevOffset = Vec3d.ZERO;

	public final SettingMode mode = dGroup.add((SettingMode) new SettingMode("Mode", 0, Arrays.asList("Linear", "\"natural\"", "Momentum")).featured());
	public final SettingSlider range = dGroup.add(new SettingSlider("Range", 4, 1, 8, 0.5));
	public final SettingSlider speed = dGroup.add(new SettingSlider("Speed", 8, 1, 25, 1));

	public final DropDown randNoiseDD = dGroup.add(new DropDown("Random Noise"));
	public final SettingToggle rnEnabled = randNoiseDD.add(new SettingToggle("Enabled", true));
	public final SettingSlider rnAmount = randNoiseDD.add(new SettingSlider("Amount", 0.4, 0.1, 1, 0.1));
	public final SettingSlider rnSpeed = randNoiseDD.add(new SettingSlider("Speed", 0.7, 0.1, 10, 0.1));
	public final SettingToggle rnSBR = randNoiseDD.add(new SettingToggle("SBR", false));
	public final SettingSlider rnSBRInfluence = randNoiseDD.add(new SettingSlider("SBR Influence", 0.8, 0.1, 2, 0.1));

	public final TargetsDropDown targetsDD = dGroup.add(new TargetsDropDown("Targets"));
	public final SortMode sortMode = dGroup.add((SortMode) new SortMode("Sort").featured());

	public AimAssist() {
		super("AimAssist", ModuleCategory.COMBAT, 0, "Controller player mode");
		noise = new PerlinNoiseSampler(Random.create());
	}

	@EventSubscribe
	public void onMouseUpdate(EventMouseUpdate event) {

		if (mc.player == null || mc.world == null)
			return;

		if (targetPos == null) return;

		int moveMode = mode.getValue();

		double speed = this.speed.getValue();

		Rotation desired = PlayerUtil.getPosRotation(mc.player, prevTargetPos.lerp(targetPos, event.timeDelta).add(prevOffset.lerp(offset, event.timeDelta)));

		float pdelta = RotationUtil.getDegreeChange(mc.player.lastPitch, desired.pitch);
		float ydelta = RotationUtil.getDegreeChange(mc.player.lastYaw, desired.yaw);

		if (moveMode == 1) {
			float combined = Math.max((ydelta + pdelta) / 25, 1);

			combined = combined * combined;

			speed *= combined * Math.abs(noise.sample(mc.world.getTime() / 1000.0f * event.timeDelta, 0.23, 0.11)) + 0.1;
			speed = Math.min(Math.max(speed, speed / 2f), 100);
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
		double distance = range.getValue();

		boolean randomOffset = rnEnabled.getValue();
		double randAmount = rnAmount.getValue();
		double randSpeed = rnSpeed.getValue();
		boolean SBR = rnSBR.getValue();
		double SBRinf = rnSBRInfluence.getValue();

		List<Entity> entities = EntityUtil.getEntities(distance, targetsDD, sortMode.getComparator());

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

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		if (targetPos == null) return;
		Color c = Colors.PALETTE_1.getColor();

		double alpha = 0.2f;
		float r = c.getRed() / 255.0f;
		float g = c.getGreen() / 255.0f;
		float b = c.getBlue() / 255.0f;

		MatrixStack matrices = event.matrixStack;

		Vec3d tpos = prevTargetPos.lerp(targetPos, event.tickDelta);
		Vec3d tposoff = tpos.add(prevOffset.lerp(offset, event.tickDelta));

		Box box = new Box(tposoff.subtract(0.2), tposoff.add(0.2));
		Box box2 = new Box(tpos.subtract(0.2), tpos.add(0.2));

		Renderer renderer = Renderer.getInstance();

		renderer.renderCube(box2, c, matrices);
		renderer.renderCubeOutlines(box2, 2, c, matrices);

		renderer.draw(BallsRenderPipelines.QUADS);
	}
}
