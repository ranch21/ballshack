package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.setting.moduleSettings.SortMode;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;
import org.ranch.ballshack.util.RotationUtil;

import java.util.Arrays;
import java.util.List;

public class AimAssist extends Module {
	public AimAssist() {
		super("AimAssist", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(4, "Range", 1, 8, 0.5),
				new SettingSlider(8, "Speed", 1, 25, 1),
				new SettingToggle(true, "Random"),
				new TargetsDropDown("Targets"),
				new SortMode("Sort").featured()
		)));
	}

	@EventSubscribe
	public void onMouseUpdate(EventMouseUpdate event) {

		double distance = (double) getSettings().getSetting(0).getValue();

		double speed = (double) getSettings().getSetting(1).getValue();

		boolean randomOffset = (boolean) getSettings().getSetting(2).getValue();

		TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(3);

		SortMode mode = (SortMode) getSettings().getSetting(4);

		List<Entity> entities = EntityUtil.getEntities(distance, targets, mode.getComparator());

		if (entities.isEmpty()) return;

		Entity e = entities.get(0);

		Rotation desired = PlayerUtil.getPosRotation(mc.player, EntityUtil.getCenter(e));

		Rotation step = RotationUtil.slowlyTurnTowards(desired, (float) speed);

		if (randomOffset) {
			float off = (float) Math.random() * 3 - 1.5f;
			step.pitch += off;
			step.yaw += off;
		}

		event.deltaX = (step.yaw - mc.player.getYaw()) + event.origDeltaX;
		event.deltaY = (step.pitch - mc.player.getPitch()) + event.origDeltaY;
	}
}
