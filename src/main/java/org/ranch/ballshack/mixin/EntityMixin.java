package org.ranch.ballshack.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventSetSneaking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "setSneaking", at = @At("HEAD"), cancellable = true)
	private void setSneaking(boolean sneaking, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity) {
			EventSetSneaking event = new EventSetSneaking(sneaking);
			BallsHack.eventBus.post(event);
			if (event.isCancelled()) {
				ci.cancel();
			}
			sneaking = event.sneaking;
		}
	}
}
