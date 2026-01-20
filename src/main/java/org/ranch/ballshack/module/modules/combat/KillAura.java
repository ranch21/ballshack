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

	public SettingSlider range = dGroup.add(new SettingSlider(4, "Range", 1, 8, 0.5));

	public DropDown multiDD = dGroup.add(new DropDown("Multi"));
	public SettingToggle mEnabled = multiDD.add(new SettingToggle(true, "Enabled"));
	public SettingSlider mTargetMax = multiDD.add(new SettingSlider(4, "Targets", 2, 10, 1));

	public TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));
	public SettingMode rotate = dGroup.add((SettingMode) new SettingMode(0, "Rotate", Arrays.asList("None", "Packet", "True")).featured());
	public SettingToggle swing = dGroup.add(new SettingToggle(true, "Swing"));
	public SortMode sort = dGroup.add(new SortMode("Sort"));

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
