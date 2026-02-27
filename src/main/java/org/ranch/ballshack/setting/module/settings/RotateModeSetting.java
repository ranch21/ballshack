package org.ranch.ballshack.setting.module.settings;

public class RotateModeSetting extends ModeSetting<RotateModeSetting.RotateMode> {

	public RotateModeSetting(String name) {
		super(name, RotateMode.NONE, RotateMode.values());
	}

	public enum RotateMode {
		NONE, PACKET, TRUE
	}
}
