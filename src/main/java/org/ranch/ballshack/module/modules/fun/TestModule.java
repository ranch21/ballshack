package org.ranch.ballshack.module.modules.fun;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.util.PlayerUtil;

public class TestModule extends Module {

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
