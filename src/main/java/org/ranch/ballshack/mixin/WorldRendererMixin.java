package org.ranch.ballshack.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(method = "renderTargetBlockOutline", at = @At("HEAD"), cancellable = true)
	private void renderTargetBlockOutline(Camera camera, VertexConsumerProvider.Immediate vertexConsumers, MatrixStack matrices, boolean translucent, CallbackInfo ci) {

		RenderSystem.getProjectionMatrix();
		Quaternionf quaternionf = camera.getRotation().conjugate(new Quaternionf());
		Matrix4f matrix4f3 = new Matrix4f().rotation(quaternionf);
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.multiplyPositionMatrix(matrix4f3);
		EventWorldRender.Outline event = new EventWorldRender.Outline(matrices, MinecraftClient.getInstance().getRenderTickCounter(), translucent, vertexConsumers);

		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}