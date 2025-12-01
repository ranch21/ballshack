package org.ranch.ballshack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
			cancellable = true
	)
	private void onRenderWorldHandRendering(
			RenderTickCounter tickCounter,
			CallbackInfo ci,
			@Local(ordinal = 1) Matrix4f matrix4f2,
			@Local(ordinal = 1) float tickDelta
	) {
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.multiplyPositionMatrix(matrix4f2);
		EventWorldRender.Post event = new EventWorldRender.Post(matrixStack, tickCounter);

		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
