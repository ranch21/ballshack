package org.ranch.ballshack.module.modules.render;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventCameraExtend;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.NumberSetting;

public class ThirdPersonCam extends Module {

	public final NumberSetting xOffset = dGroup.add(new NumberSetting("XOffset", 4).min(0).max(10).step(0.25));
	public final NumberSetting yOffset = dGroup.add(new NumberSetting("YOffset", 0).min(-5).max(5).step(0.25));
	public final NumberSetting zOffset = dGroup.add(new NumberSetting("ZOffset", 0).min(-5).max(5).step(0.25));

	public ThirdPersonCam() {
		super("BetterF5", ModuleCategory.RENDER, 0, "stuff it into the wall");
	}

	@EventSubscribe
	public void onCameraExtend(EventCameraExtend event) {
		event.f = -xOffset.getValueFloat();
		event.g = yOffset.getValueFloat();
		event.h = zOffset.getValueFloat();
	}
}
