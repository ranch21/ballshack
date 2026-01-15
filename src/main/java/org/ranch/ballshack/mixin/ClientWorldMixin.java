package org.ranch.ballshack.mixin;

import net.minecraft.client.world.ClientWorld;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.debug.renderers.PathDebugRenderer;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.util.EntitySim;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.FreelookHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@Inject(at = @At("HEAD"), method = "tickEntities")
	private void tickEntities(CallbackInfo info) {
		if (!FreelookHandler.enabled && BallsHack.mc.player != null)
			FreelookHandler.setRotation(EntityUtil.getRotation(BallsHack.mc.player));
		BallsHack.eventBus.post(new EventTick());

		PathDebugRenderer debugRenderer = (PathDebugRenderer) DebugRenderers.getRenderer("playersim");
		if (debugRenderer.getEnabled())
			debugRenderer.setData(EntitySim.simulatePlayer(BallsHack.mc.player, 10), new Color(255,255,255, 50));
	}
}