package org.ranch.ballshack.mixin;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.FogRenderer;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventFog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
	@Inject(method = "getCameraSubmersionType", at = @At(value = "RETURN"), cancellable = true)
	private void getCameraSubmersionType(Camera camera, CallbackInfoReturnable<CameraSubmersionType> cir) {
		EventFog.Submersion event = new EventFog.Submersion(cir.getReturnValue());
		BallsHack.eventBus.post(event);
		cir.setReturnValue(event.submersionType);
	}
}
