package org.ranch.ballshack.module.modules.fun;

import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.ModuleSettingsGroup;
import org.ranch.ballshack.setting.module.settings.BooleanSetting;
import org.ranch.ballshack.setting.module.settings.ModeSetting;
import org.ranch.ballshack.setting.module.settings.NumberSetting;
import org.ranch.ballshack.setting.module.settings.StringSetting;
import org.ranch.ballshack.util.PlayerUtil;

public class TestModule extends Module {

	public static enum EnumTest {
		ONE, TWO, THREE
	}

	public final NumberSetting slider = dGroup.add(new NumberSetting("Slider", 10).min(0).max(20));
	public final BooleanSetting toggle = dGroup.add(new BooleanSetting("Toggle", false));
	public final ModeSetting<EnumTest> mode = dGroup.add(new ModeSetting<>("Mode", EnumTest.ONE, EnumTest.values()));
	public final StringSetting string = dGroup.add(new StringSetting("String", "hello balls"));

	public final ModuleSettingsGroup oGroup = addGroup(new ModuleSettingsGroup("Other"));
	public final NumberSetting slider2 = oGroup.add(new NumberSetting("Slider", 10).min(0).max(20));
	public final BooleanSetting toggle2 = oGroup.add(new BooleanSetting("Toggle", false));
	public final NumberSetting slider3 = oGroup.add(new NumberSetting("Slider", 10).min(0).max(20));
	public final BooleanSetting toggle4 = oGroup.add(new BooleanSetting("Toggle", false));

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
