package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.Arrays;

public class KillAura extends Module {

	public final SettingSlider range = dGroup.add(new SettingSlider("Range", 4, 1, 8, 0.5));

	public final DropDown multiDD = dGroup.add(new DropDown("Multi"));
	public final SettingToggle mEnabled = multiDD.add(new SettingToggle("Enabled", false));
	public final SettingSlider mTargetMax = multiDD.add(new SettingSlider("Targets", 2, 2, 10, 1));

	public final TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));
	public final SettingMode rotate = dGroup.add( new SettingMode("Rotate", 0, Arrays.asList("None", "Packet", "True")).featured());
	public final SettingToggle swing = dGroup.add(new SettingToggle("Swing", true));
	public final SortMode sort = dGroup.add(new SortMode("Sort"));

	public KillAura() {
		super("KillAura", ModuleCategory.COMBAT, 0, "Repel players");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.getAttackCooldownProgress(0.5f) == 1.0f) {

			int attacked = 0;

			double distance = range.getValue();

			for (Entity e : EntityUtil.getEntities(distance, targets, sort.getComparator())) {

				int mode = rotate.getValue();

				if (mode == 1) {
					PlayerUtil.facePosPacket(EntityUtil.getCenter(e));
				} else if (mode == 2) {
					PlayerUtil.facePos(EntityUtil.getCenter(e));
				}

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
}
