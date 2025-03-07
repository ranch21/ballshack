package org.ranch.ballshack.module.modules;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingToggle;
import org.ranch.ballshack.setting.settings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;

import java.util.Arrays;

public class TriggerBot extends Module {
	public TriggerBot() {
		super("TriggerBot", ModuleCategory.COMBAT, 0, new ModuleSettings(Arrays.asList(
				new SettingToggle(true , "Swing"),
				new TargetsDropDown("Targets")
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.getAttackCooldownProgress(0.5f) == 1.0f) {
			HitResult h = mc.crosshairTarget;
			if (h instanceof EntityHitResult entityHitResult) {
				Entity e = entityHitResult.getEntity();

				TargetsDropDown targets = (TargetsDropDown) getSettings().getSetting(1);

				if (EntityUtil.filterByType(e, targets)) {
					mc.interactionManager.attackEntity(mc.player, e);
					boolean swing = (boolean) getSettings().getSetting(0).getValue();
					if (swing) mc.player.swingHand(Hand.MAIN_HAND);
				}
			}
		}
	}
}
