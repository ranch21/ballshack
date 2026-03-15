package org.ranch.ballshack.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventGamma;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

	@Redirect(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F"))
	public float getValue(Double instance) {
		EventGamma event = new EventGamma(instance);
		BallsHack.eventBus.post(event);
		return (float) event.gamma;
	}
}