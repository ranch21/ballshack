package org.ranch.ballshack.module.modules.render;

import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventFog;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.BooleanSetting;

public class NoRender extends Module {

	public final BooleanSetting waterFog = dGroup.add(new BooleanSetting("Water Fog", true));
	public final BooleanSetting lavaFog = dGroup.add(new BooleanSetting("Lava Fog", true));
	public final BooleanSetting snowFog = dGroup.add(new BooleanSetting("Snow Fog", true));


	public NoRender() {
		super("NoRender", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onFogSubmersion(EventFog.Submersion event) {
		if (!waterFog.getValue() && event.submersionType == CameraSubmersionType.WATER) {
			event.submersionType = CameraSubmersionType.ATMOSPHERIC;
		}
		if (!lavaFog.getValue() && event.submersionType == CameraSubmersionType.LAVA) {
			event.submersionType = CameraSubmersionType.ATMOSPHERIC;
		}
		if (!snowFog.getValue() && event.submersionType == CameraSubmersionType.POWDER_SNOW) {
			event.submersionType = CameraSubmersionType.ATMOSPHERIC;
		}
	}
}
