package org.ranch.ballshack.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventReach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

	@Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
	private void getReachDistance(CallbackInfoReturnable<Float> cir) {
		EventReach event = new EventReach(cir.getReturnValueF());
		BallsHack.eventBus.post(event);

		//BallsLogger.info(event.getReach() + "");

		cir.setReturnValue(event.getReach());
	}
}
