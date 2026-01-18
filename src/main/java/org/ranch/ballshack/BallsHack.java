package org.ranch.ballshack;

import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.event.EventBus;
import org.ranch.ballshack.gui.ClickGuiScreen;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.SettingSaver;
import org.ranch.ballshack.setting.SettingsManager;

public class BallsHack implements ModInitializer {

	public static EventBus eventBus = new EventBus();

	public static Setting<String> title = new Setting<>("BallsHack", "watermark", new TypeToken<String>() {
	}.getType());
	public static String version = "1.31";

	public static String ID = "ballshack";

	public static MinecraftClient mc = Constants.mc;

	@Override
	public void onInitialize() {
		SettingSaver.init();
		SettingsManager.registerSetting(FriendManager.setting);
		SettingsManager.registerSetting(DebugRenderers.enabled);
		SettingsManager.registerSetting(ClickGuiScreen.windowData);
		SettingsManager.registerSetting(title);
		SettingSaver.readSettings();
		FriendManager.set();
		DebugRenderers.load();
	}
}
