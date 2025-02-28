package org.ranch.ballshack.mixin;

import net.minecraft.client.world.ClientWorld;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@Inject(at = @At("HEAD"), method = "tickEntities")
	private void init(CallbackInfo info) {
		BallsHack.eventBus.post(new EventTick());
	}
}