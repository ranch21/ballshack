package org.ranch.ballshack;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import org.ranch.ballshack.event.EventBus;
import org.ranch.ballshack.setting.SettingSaver;

public class BallsHack implements ModInitializer {

	public static EventBus eventBus = new EventBus();
	private static KeyBinding keyBinding;

	public static String title = "BallsHack";
	public static String version = "1.25";

	@Override
	public void onInitialize() {
		SettingSaver.init();
		SettingSaver.readModules();
	}
}
