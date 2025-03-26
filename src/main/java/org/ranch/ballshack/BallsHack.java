package org.ranch.ballshack;

import net.fabricmc.api.ModInitializer;
import org.ranch.ballshack.event.EventBus;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.SettingSaver;
import org.ranch.ballshack.setting.SettingsManager;

public class BallsHack implements ModInitializer {

	public static EventBus eventBus = new EventBus();

	public static Setting<String> title = new Setting<>("BallsHack", "watermark");
	public static String version = "1.29";

	@Override
	public void onInitialize() {
		SettingSaver.init();
		SettingsManager.registerSetting(FriendManager.setting);
		SettingsManager.registerSetting(title);
		SettingSaver.readSettings();
		FriendManager.set();
	}
}
