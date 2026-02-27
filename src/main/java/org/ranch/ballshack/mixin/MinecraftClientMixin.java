package org.ranch.ballshack.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.event.events.EventWorld;
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

	@Inject(method = "joinWorld", at = @At(value = "RETURN"))
	public void joinWorld(ClientWorld world, CallbackInfo ci) {
		BallsHack.eventBus.post(new EventWorld.Join());
	}

	@Inject(method = "disconnect(Lnet/minecraft/text/Text;)V", at = @At(value = "RETURN"))
	public void disconnect(Text reasonText, CallbackInfo ci) {
		BallsHack.eventBus.post(new EventWorld.Leave());
	}
}
