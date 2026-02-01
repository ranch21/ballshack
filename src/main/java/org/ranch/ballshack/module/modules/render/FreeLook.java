package org.ranch.ballshack.module.modules.render;

import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.FreelookHandler;

public class FreeLook extends Module {

	public final SettingToggle rotateInput = dGroup.add(new SettingToggle("RotateInput", true));
	public final SettingToggle snapInput = dGroup.add(new SettingToggle("SnapInput", false).depends(rotateInput::getValue));

	public FreeLook() {
		super("FreeLook", ModuleCategory.RENDER, 0, "goteteds");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		FreelookHandler.enable();
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		FreelookHandler.rotateInput = rotateInput.getValue();
		FreelookHandler.snapInput = snapInput.getValue();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		FreelookHandler.disable();
	}
}
