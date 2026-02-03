package org.ranch.ballshack.module.modules.render;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventCameraExtend;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;

public class ThirdPersonCam extends Module {

	public final SettingSlider xOffset = dGroup.add(new SettingSlider("XOffset", 4, 0, 10, 0.25));
	public final SettingSlider yOffset = dGroup.add(new SettingSlider("YOffset", 0, -5, 5, 0.25));
	public final SettingSlider zOffset = dGroup.add(new SettingSlider("ZOffset", 0, -5, 5, 0.25));

	public ThirdPersonCam() {
		super("ThirdPersonCam", ModuleCategory.RENDER, 0, "stuff it into the wall");
	}

	@EventSubscribe
	public void onCameraExtend(EventCameraExtend event) {
		event.f = -xOffset.getValueFloat();
		event.g = yOffset.getValueFloat();
		event.h = zOffset.getValueFloat();
	}
}
