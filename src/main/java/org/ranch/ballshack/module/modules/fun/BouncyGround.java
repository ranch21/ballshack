package org.ranch.ballshack.module.modules.fun;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingSlider;

import java.util.Arrays;

public class BouncyGround extends Module {
	public BouncyGround() {
		super("Bounce", ModuleCategory.FUN, 0, new ModuleSettings(Arrays.asList(
				new SettingSlider(1,"Amount",0.25,10, 0.25)
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.isOnGround()) {
			Vec3d vel = mc.player.getVelocity();
			double amount = (double) getSettings().getSetting(0).getValue();
			mc.player.setVelocity(vel.x, amount, vel.z);
		}
	}
}
