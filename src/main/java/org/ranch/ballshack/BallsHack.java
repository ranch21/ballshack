package org.ranch.ballshack;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import org.ranch.ballshack.event.EventBus;

public class BallsHack implements ModInitializer {

	public static EventBus eventBus = new EventBus();
	private static KeyBinding keyBinding;

	@Override
	public void onInitialize() {

	}
}
