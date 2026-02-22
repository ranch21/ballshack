package org.ranch.ballshack;

import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.event.EventBus;
import org.ranch.ballshack.gui.ThemeManager;
import org.ranch.ballshack.gui.neko.Neko;
import org.ranch.ballshack.gui.windows.clickgui.ClickGuiScreen;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.Themes;
import org.ranch.ballshack.setting.ModuleSettingSaver;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.SettingsManager;
import org.ranch.ballshack.util.DatabaseFetcher;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BallsHack implements ModInitializer {

	public static final EventBus eventBus = new EventBus();;

	public static final Setting<String> title = new Setting<>("BallsHack", "watermark", new TypeToken<String>() {
	}.getType());
	public static final String version = "1.33";

	public static final String ID = "ballshack";

	public static final MinecraftClient mc = Constants.mc;

	public static final Neko neko = new Neko();

	@Override
	public void onInitialize() {
		SettingsManager.registerSetting(ThemeManager.loaded);
		SettingsManager.registerSetting(FriendManager.setting);
		SettingsManager.registerSetting(DebugRenderers.enabled);
		SettingsManager.registerSetting(ClickGuiScreen.windowData);
		SettingsManager.registerSetting(title);
		SettingsManager.registerSetting(CommandManager.prefix);
		DatabaseFetcher.registerSettings();

		if (ThemeManager.loaded.getValue()) {
			ThemeManager.loadTheme((ModuleManager.getModuleByClass(Themes.class)).theme.getValue());
		}

		FriendManager.set();
		DebugRenderers.load();
		ModuleSettingSaver.load();
	}

	public static Path getSaveDir() {
		return Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "ballshack/");
	}
}
