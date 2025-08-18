package org.ranch.ballshack.mixin;

import net.minecraft.client.Mouse;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

	@Shadow
	private double cursorDeltaX;
	@Shadow
	private double cursorDeltaY;

	@Inject(method = "updateMouse", at = @At(value = "HEAD"))
	public void updateMouse(double timeDelta, CallbackInfo ci) {
		EventMouseUpdate event = new EventMouseUpdate(cursorDeltaX, cursorDeltaY, timeDelta);
		BallsHack.eventBus.post(event);
		cursorDeltaX = event.deltaX;
		cursorDeltaY = event.deltaY;
	}
}
