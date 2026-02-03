package org.ranch.ballshack.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventWalkOnFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "canWalkOnFluid", at = @At("RETURN"), cancellable = true)
	private void canWalkOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof ClientPlayerEntity) {
			EventWalkOnFluid event = new EventWalkOnFluid(cir.getReturnValue(), state);
			BallsHack.eventBus.post(event);
			if (event.isCancelled()) {
				cir.cancel();
			}
			cir.setReturnValue(event.can);
		}
	}
}
