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
import org.ranch.ballshack.setting.module.ModuleSettingsGroup;
import org.ranch.ballshack.setting.module.TargetsSettingGroup;
import org.ranch.ballshack.setting.module.settings.BooleanSetting;
import org.ranch.ballshack.setting.module.settings.NumberSetting;
import org.ranch.ballshack.setting.module.settings.RotateModeSetting;
import org.ranch.ballshack.setting.module.settings.SortModeSetting;
import org.ranch.ballshack.util.*;

public class KillAura extends Module {

	public final NumberSetting range = dGroup.add(new NumberSetting("Range", 4).min(1).max(8).step(0.5));

	public final ModuleSettingsGroup mGroup = addGroup(new ModuleSettingsGroup("Multi"));
	public final BooleanSetting mEnabled = mGroup.add(new BooleanSetting("Enabled", false));
	public final NumberSetting mTargetMax = mGroup.add(new NumberSetting("Targets", 2).min(2).max(10).step(1));

	public final TargetsSettingGroup targets = addGroup(new TargetsSettingGroup("Targets"));
	public final RotateModeSetting rotate = dGroup.add(new RotateModeSetting("Rotate"));

	public final BooleanSetting randomNoise = dGroup.add(new BooleanSetting("Noise", true).depends(() -> rotate.getValue() == RotateModeSetting.RotateMode.TRUE));
	public final BooleanSetting freeLook = dGroup.add(new BooleanSetting("FreeLook", true).depends(() -> rotate.getValue() == RotateModeSetting.RotateMode.TRUE));
	public final BooleanSetting slowRotate = dGroup.add(new BooleanSetting("SlowRotate", true).depends(() -> rotate.getValue() == RotateModeSetting.RotateMode.TRUE));
	public final NumberSetting rotSpeed = dGroup.add(new NumberSetting("RotateSpeed", 5).min(2).max(60).step(1).depends(slowRotate::getValue));
	public final BooleanSetting swing = dGroup.add(new BooleanSetting("Swing", true));
	public final SortModeSetting sort = dGroup.add(new SortModeSetting("Sort"));

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

			RotateModeSetting.RotateMode mode = rotate.getValue();
			boolean attack = false;

			switch (mode) {
				case NONE:
					attack = canAttack;
					break;
				case PACKET:
					if (canAttack)
						PlayerUtil.facePosPacket(EntityUtil.getCenter(e));
					attack = canAttack;
					break;
				case TRUE:
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
