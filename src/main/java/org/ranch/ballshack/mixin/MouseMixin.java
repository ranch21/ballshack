package org.ranch.ballshack.mixin;

import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventMouseUpdate;
import org.ranch.ballshack.util.FreelookHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

	@Shadow
	private double cursorDeltaX;
	@Shadow
	private double cursorDeltaY;

	@Shadow
	private double y;

	@Inject(method = "updateMouse", at = @At(value = "HEAD"))
	public void updateMouse(double timeDelta, CallbackInfo ci) {
		EventMouseUpdate event = new EventMouseUpdate.Mouse(cursorDeltaX, cursorDeltaY, timeDelta);
		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			cursorDeltaX = 0;
			cursorDeltaY = 0;
		} else {
			cursorDeltaX = event.deltaX;
			cursorDeltaY = event.deltaY;
		}
	}

	@Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
	public void changeLookDirection(ClientPlayerEntity instance, double yaw, double pitch) {
		EventMouseUpdate event = new EventMouseUpdate.Entity(yaw, pitch);
		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			yaw = 0;
			pitch = 0;
		} else {
			yaw = event.deltaX;
			pitch = event.deltaY;
		}
		if (FreelookHandler.getEnabled()) {
			FreelookHandler.updateDirection(yaw, pitch);
		} else {
			instance.changeLookDirection(yaw, pitch);
		}
	}
}
