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
import org.ranch.ballshack.setting.ClientSetting;
import org.ranch.ballshack.setting.ClientSettingSaver;
import org.ranch.ballshack.util.DatabaseFetcher;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BallsHack implements ModInitializer {

	public static final EventBus eventBus = new EventBus();;

	public static final ClientSetting<String> title = new ClientSetting<>("watermark", "BallsHack");
	public static final String version = "1.33";

	public static final String ID = "ballshack";

	public static final MinecraftClient mc = Constants.mc;

	public static final Neko neko = new Neko();

	@Override
	public void onInitialize() {
		ClientSettingSaver.registerSetting(ThemeManager.loaded);
		ClientSettingSaver.registerSetting(FriendManager.setting);
		ClientSettingSaver.registerSetting(DebugRenderers.enabled);
		ClientSettingSaver.registerSetting(title);
		ClientSettingSaver.registerSetting(CommandManager.prefix);
		DatabaseFetcher.registerSettings();

		if (ThemeManager.loaded.getValue()) {
			ThemeManager.loadTheme((ModuleManager.getModuleByClass(Themes.class)).theme.getValue());
		}

		FriendManager.set();
		DebugRenderers.load();
		ClientSettingSaver.load();
		ModuleSettingSaver.load();
	}

	public static Path getSaveDir() {
		return Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "ballshack/");
	}
}
