package org.ranch.ballshack.module.modules.fun;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.moduleSettings.*;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;
import org.ranch.ballshack.util.RotationUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TestModule extends Module {

	ModuleSettingsGroup defaultGroup = settings.getDefaultGroup();

	private SettingSlider testSlider = defaultGroup.add(new SettingSlider(1, "test", 0, 10, 1));
	private DropDown dropDown = defaultGroup.add(new DropDown("YOOOWHAT"));
	private SettingToggle testToggle = dropDown.settings.add(new SettingToggle(false, "testToggle"));

	public TestModule() {
		super("Test", ModuleCategory.FUN, 0, "Testingh ahwdhghfi");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		mc.player.setYaw(mc.player.getYaw() + 1.0f); //the first thing a module did
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerMovementVector event) {
		PlayerUtil.setMovement(new Vec3d(0, 0, 1), event);
	}
}
