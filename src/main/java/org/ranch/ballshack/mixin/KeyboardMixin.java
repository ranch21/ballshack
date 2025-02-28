package org.ranch.ballshack.mixin;

import net.minecraft.client.Keyboard;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventKeyPress;
import org.ranch.ballshack.module.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At(value = "INVOKE", target = "net/minecraft/client/util/InputUtil.isKeyPressed(JI)Z", ordinal = 5))
	public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		ModuleManager.handleKeyPress(key);
		BallsHack.eventBus.post(new EventKeyPress(key));
	}
}