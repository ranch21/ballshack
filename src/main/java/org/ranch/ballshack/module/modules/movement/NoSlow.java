package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventSlow;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.NumberSetting;

public class NoSlow extends Module {

	public final NumberSetting multiplier = dGroup.add(new NumberSetting("Multiplier", 0.5).min(0).max(2).step(0.1));

	public NoSlow() {
		super("NoSlow", ModuleCategory.MOVEMENT, 0);
	}

	@EventSubscribe
	public void onSlow(EventSlow event) {
		if (multiplier.getValue() == 0) {
			event.mult = new Vec3d(0, 0, 0);
		} else {
			event.mult = new Vec3d(
					Math.min(event.mult.x / multiplier.getValue(), 1),
					Math.min(event.mult.y / multiplier.getValue(), 1),
					Math.min(event.mult.z / multiplier.getValue(), 1)
			);
		}
	}
}
