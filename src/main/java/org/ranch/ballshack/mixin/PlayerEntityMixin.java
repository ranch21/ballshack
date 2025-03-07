package org.ranch.ballshack.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventReach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Inject(method = "getBlockInteractionRange", at = @At("RETURN"), cancellable = true)
	private void getBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
		EventReach event = new EventReach(cir.getReturnValueD());
		BallsHack.eventBus.post(event);
		cir.setReturnValue(event.reach);
	}

	@Inject(method = "getEntityInteractionRange", at = @At("RETURN"), cancellable = true)
	private void getEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
		EventReach event = new EventReach(cir.getReturnValueD());
		BallsHack.eventBus.post(event);
		cir.setReturnValue(event.reach);
	}
}
