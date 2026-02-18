package org.ranch.ballshack.module.modules.render;

import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacket;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.DropDown;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

public class Environment extends Module {
	public Environment() {
		super("Environment", ModuleCategory.RENDER, 0, "Change the world");
	}

	public final DropDown timeDD = dGroup.add(new DropDown("Time"));
	public final SettingToggle overrideTime = timeDD.add(new SettingToggle("Override", false));
	public final SettingSlider time = timeDD.add(new SettingSlider("Speed", 0, 0, 24000, 1000));

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
