package org.ranch.ballshack.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "setScreen", at = @At(value = "HEAD"))
	public void setScreen(Screen screen, CallbackInfo ci) {
		EventScreen.Init event = new EventScreen.Init(screen);
		BallsHack.eventBus.post(event);
	}
}
