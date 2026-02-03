package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.*;

import java.util.Arrays;

public class KillAura extends Module {

	public final SettingSlider range = dGroup.add(new SettingSlider("Range", 4, 1, 8, 0.5));

	public final DropDown multiDD = dGroup.add(new DropDown("Multi"));
	public final SettingToggle mEnabled = multiDD.add(new SettingToggle("Enabled", false));
	public final SettingSlider mTargetMax = multiDD.add(new SettingSlider("Targets", 2, 2, 10, 1));

	public final TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));
	public final SettingMode rotate = dGroup.add(new SettingMode("Rotate", 0, Arrays.asList("None", "Packet", "True")).featured());
	public final SettingToggle randomNoise = dGroup.add(new SettingToggle("Noise", true).depends(() -> rotate.getValue() == 2));
	public final SettingToggle freeLook = dGroup.add(new SettingToggle("FreeLook", true).depends(() -> rotate.getValue() == 2));
	public final SettingToggle slowRotate = dGroup.add(new SettingToggle("SlowRotate", true).depends(() -> rotate.getValue() == 2));
	public final SettingSlider rotSpeed = dGroup.add(new SettingSlider("RotateSpeed", 5, 2, 60, 1).depends(slowRotate::getValue));
	public final SettingToggle swing = dGroup.add(new SettingToggle("Swing", true));
	public final SortMode sort = dGroup.add(new SortMode("Sort"));

	public KillAura() {
		super("KillAura", ModuleCategory.COMBAT, 0, "Repel players");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		int attacked = 0;
		boolean canAttack = mc.player.getAttackCooldownProgress(0.5f) == 1.0f;
		double distance = range.getValue();

		FreelookHandler.disable();

		for (Entity e : EntityUtil.getEntities(distance, targets, sort.getComparator(), true)) {

			FreelookHandler.setEnabled(freeLook.getValue() && freeLook.dependencyMet());

			int mode = rotate.getValue();
			boolean attack = false;

			switch (mode) {
				case 0:
					attack = canAttack;
					break;
				case 1:
					if (canAttack)
						PlayerUtil.facePosPacket(EntityUtil.getCenter(e));
					attack = canAttack;
					break;
				case 2:
					if (mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
						EntityHitResult result = (EntityHitResult) mc.crosshairTarget;
						attack = result.getEntity().equals(e) && canAttack;
					}

					Vec3d tPoint = EntityUtil.getCenter(e);

					if (randomNoise.getValue())
						tPoint = tPoint.add(EntityUtil.getNoiseOffset(1, Math.min(e.getHeight(), e.getWidth())));

					if (slowRotate.getValue()) {
						Rotation target = PlayerUtil.getPosRotation(mc.player, tPoint);
						Rotation step = RotationUtil.slowlyTurnTowards(target, rotSpeed.getValueFloat());
						mc.player.setYaw((step.yaw - mc.player.getYaw()) + mc.player.getYaw());
						mc.player.setPitch((step.pitch - mc.player.getPitch()) + mc.player.getPitch());
					} else {
						attack = canAttack;
						PlayerUtil.facePos(tPoint);
					}
					break;
			}

			if (attack) {
				mc.interactionManager.attackEntity(mc.player, e);

				boolean swing = this.swing.getValue();
				if (swing) mc.player.swingHand(Hand.MAIN_HAND);

				attacked++;

				if (attacked >= mTargetMax.getValue() && mEnabled.getValue()) {
					break;
				}

				if (!mEnabled.getValue()) {
					break;
				}
			}
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		FreelookHandler.disable();
	}
}
