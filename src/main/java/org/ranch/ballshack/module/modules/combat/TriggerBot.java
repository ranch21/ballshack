package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;

import java.util.Arrays;

public class TriggerBot extends Module {
	private static int cooldown = 0;

	public TriggerBot() {
		super("TriggerBot", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingToggle(true, "Swing"),
				new SettingSlider(2, "MaxRandDelay", 0, 10, 1),
				new TargetsDropDown("Targets")
		)), "Monkey in computer press when enemy");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.getAttackCooldownProgress(0.5f) == 1.0f) {
			HitResult h = mc.crosshairTarget;

			if (h instanceof EntityHitResult entityHitResult) {
				Entity e = entityHitResult.getEntity();

				TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(2);

				if (EntityUtil.filterByType(e, targets)) {
					if (cooldown-- <= 0) {
						mc.interactionManager.attackEntity(mc.player, e);
						boolean swing = (boolean) getSettings().getSetting(0).getValue();
						if (swing) mc.player.swingHand(Hand.MAIN_HAND);
						cooldown = (int) (double) getSettings().getSetting(1).getValue();
					}
				}
			}
		}
	}
}
