package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.*;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.Arrays;

public class TpAura extends Module {

	int delay = 0;

	public TpAura() {
		super("TpAura", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(4, "Range", 2, 20, 1),
				new TargetsDropDown("Targets"),
				new SettingMode(0, "Rotate", Arrays.asList("None", "Packet", "True")).featured(),
				new SettingToggle(true, "Swing"),
				new SortMode("Sort"),
				new SettingSlider(2, "Delay", 1, 4, 1)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {

		double distance = (double) getSettings().getSetting(0).getValue();

		TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(1);

		SortMode sort = (SortMode) getSettings().getSetting(4);

		Entity e = EntityUtil.getEntities(distance, targets, sort.getComparator()).get(0);

		int mode = (int) getSettings().getSetting(2).getValue();

		if (mode == 1) {
			PlayerUtil.facePosPacket(EntityUtil.getCenter(e));
		} else if (mode == 2) {
			PlayerUtil.facePos(EntityUtil.getCenter(e));
		}

		if (delay >= (int) (double) getSettings().getSetting(5).getValue()) {
			tp(e, 0);
			delay = 0;
		}

		delay++;

		if (mc.player.getAttackCooldownProgress(0.5f) == 1.0f) {
			mc.interactionManager.attackEntity(mc.player, e);

			boolean swing = (boolean) getSettings().getSetting(3).getValue();
			if (swing) mc.player.swingHand(Hand.MAIN_HAND);
		}
	}

	private void tp(Entity e, int d) {

		if (d > 5) return;

		Vec3d pos = e.getPos();

		double ang = Math.toRadians(Math.random() * 360);

		double x = pos.x + (Math.cos(ang) * 3);
		double z = pos.z + (Math.sin(ang) * 3);

		double y = pos.y + Math.random() * 2;

		mc.player.setPosition(x, y, z);

		if (!mc.player.canSee(e)) {
			d++;
			tp(e, d);
		}
	}
}
