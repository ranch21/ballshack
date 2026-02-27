package org.ranch.ballshack.module.modules.render;

import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacket;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.ModuleSettingsGroup;
import org.ranch.ballshack.setting.module.settings.BooleanSetting;
import org.ranch.ballshack.setting.module.settings.NumberSetting;

public class Environment extends Module {
	public Environment() {
		super("Environment", ModuleCategory.RENDER, 0, "Change the world");
	}

	public final ModuleSettingsGroup tGroup = addGroup(new ModuleSettingsGroup("Time"));
	public final BooleanSetting overrideTime = tGroup.add(new BooleanSetting("Override", false));
	public final NumberSetting time = tGroup.add(new NumberSetting("Time", 0).min(0).max(24000).step(1000));

	@EventSubscribe
	public void onTick(EventTick event) {
		if (overrideTime.getValue())
			mc.world.setTime(time.getValueInt(), time.getValueInt(), false);
	}

	@EventSubscribe
	public void onPacketReceived(EventPacket.Receive event) {
		if (event.packet instanceof WorldTimeUpdateS2CPacket && overrideTime.getValue()) {
			event.cancel();
		}
	}
}
