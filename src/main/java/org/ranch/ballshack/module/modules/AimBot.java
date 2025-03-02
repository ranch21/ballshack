package org.ranch.ballshack.module.modules;

import net.minecraft.entity.Entity;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.setting.settings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;

import java.util.Arrays;
import java.util.List;

public class AimBot extends Module {
	public AimBot() {
		super("AimBot", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(4, "Range", 1, 8, 0.5),
				new TargetsDropDown("Targets")
		)));
	}

	@EventSubscribe
	public void onMouseUpdate(EventMouseUpdate event) {

		double distance = (double) getSettings().getSetting(0).getValue();

		TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(1);

		List<Entity> entities = EntityUtil.getEntities(distance, targets);

		if (entities.isEmpty()) return;

		Entity e = entities.get(0);

		Rotation desired = PlayerUtil.getPosRotation(mc.player, EntityUtil.getCenter(e));


		event.deltaX = (desired.yaw - mc.player.getYaw()) + event.origDeltaX;
		event.deltaY = (desired.pitch - mc.player.getPitch()) + event.origDeltaY;
	}
}
