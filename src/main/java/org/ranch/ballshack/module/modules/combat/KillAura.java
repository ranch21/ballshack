package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.Arrays;

public class KillAura extends Module {
	public KillAura() {
		super("KillAura", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(4, "Range", 1, 8, 0.5),
				new DropDown("Multi", Arrays.asList(
						new SettingToggle(true, "Enabled"),
						new SettingSlider(4, "Targets", 2, 10, 1)
				)),
				new TargetsDropDown("Targets"),
				new SettingMode(0, "Rotate", Arrays.asList("None", "Packet", "True")).featured(),
				new SettingToggle(true, "Swing"),
				new SortMode("Sort")
		)), "Repel players");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.getAttackCooldownProgress(0.5f) == 1.0f) {

			int attacked = 0;

			double distance = (double) getSettings().getSetting(0).getValue();

			TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(2);

			SortMode sort = (SortMode) getSettings().getSetting(5);

			for (Entity e : EntityUtil.getEntities(distance, targets, sort.getComparator())) {

				int mode = (int) getSettings().getSetting(3).getValue();

				if (mode == 1) {
					PlayerUtil.facePosPacket(EntityUtil.getCenter(e));
				} else if (mode == 2) {
					PlayerUtil.facePos(EntityUtil.getCenter(e));
				}

				mc.interactionManager.attackEntity(mc.player, e);

				boolean swing = (boolean) getSettings().getSetting(4).getValue();
				if (swing) mc.player.swingHand(Hand.MAIN_HAND);

				attacked++;

				DropDown dropDown = (DropDown) getSettings().getSetting(1);

				if (attacked >= (int) (double) dropDown.getSetting(1).getValue() && (boolean) dropDown.getSetting(0).getValue()) {
					break;
				}

				if (!(boolean) dropDown.getSetting(0).getValue()) {
					break;
				}
			}
		}

	}
}
