package org.ranch.ballshack;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import org.ranch.ballshack.event.EventBus;

public class BallsHack implements ModInitializer {

	public static EventBus eventBus = new EventBus();
	private static KeyBinding keyBinding;

	public static String title = "BallsHack";
	public static String version = "1.0";

	@Override
	public void onInitialize() {

	}
}
