package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;

public class TriggerBot extends Module {
	private static int cooldown = 0;

	public SettingToggle swing = dGroup.add(new SettingToggle(true, "Swing"));
	public SettingSlider maxRandDelay = dGroup.add((SettingSlider) new SettingSlider(2, "MaxRandDelay", 0, 10, 1).tooltip("i lied lol this aint random"));
	public TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));

	public TriggerBot() {
		super("TriggerBot", ModuleCategory.COMBAT, 0, "Monkey in computer press when enemy");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.getAttackCooldownProgress(0.5f) == 1.0f) {
			HitResult h = mc.crosshairTarget;

			if (h instanceof EntityHitResult entityHitResult) {
				Entity e = entityHitResult.getEntity();

				if (EntityUtil.filterByType(e, targets)) {
					if (cooldown-- <= 0) {
						mc.interactionManager.attackEntity(mc.player, e);
						if (swing.getValue()) mc.player.swingHand(Hand.MAIN_HAND);
						cooldown = (int) (double) maxRandDelay.getValue();
					}
				}
			}
		}
	}
}
