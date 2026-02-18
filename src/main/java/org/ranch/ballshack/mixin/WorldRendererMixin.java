package org.ranch.ballshack.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(method = "renderTargetBlockOutline", at = @At("HEAD"), cancellable = true)
	private void renderTargetBlockOutline(VertexConsumerProvider.Immediate immediate, MatrixStack matrices, boolean renderBlockOutline, WorldRenderState renderStates, CallbackInfo ci) {

		EventWorldRender.Outline event = new EventWorldRender.Outline(matrices, MinecraftClient.getInstance().getRenderTickCounter(), renderBlockOutline, immediate);

		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "pushEntityRenders", at = @At("HEAD"), cancellable = true)
	private void renderMain(MatrixStack matrices, WorldRenderState renderStates, OrderedRenderCommandQueue queue, CallbackInfo ci) {

		EventWorldRender.Entity event = new EventWorldRender.Entity(new MatrixStack(), MinecraftClient.getInstance().getRenderTickCounter(), renderStates, queue);

		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}