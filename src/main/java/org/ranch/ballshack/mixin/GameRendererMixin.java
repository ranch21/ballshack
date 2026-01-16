package org.ranch.ballshack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.debug.renderers.PlayerSimDebugRenderer;
import org.ranch.ballshack.debug.renderers.ScaffoldDebugRenderer;
import org.ranch.ballshack.debug.renderers.VecDebugRenderer;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.util.rendering.Renderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;

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

		VecDebugRenderer debugInput = (VecDebugRenderer) DebugRenderers.getRenderer("input");
		Vec2f input = BallsHack.mc.player.input.getMovementInput();
		Vec3d input3 = new Vec3d(input.x, 0, input.y);
		debugInput.setData(BallsHack.mc.player.getLerpedPos(tickCounter.getTickProgress(false)), input3.rotateY((float) Math.toRadians(-BallsHack.mc.player.getYaw())), new Color(0, 0, 255));
		if (debugInput.getEnabled())
			debugInput.render3d(Renderer.getInstance(), matrixStack);

		VecDebugRenderer debugVel = (VecDebugRenderer) DebugRenderers.getRenderer("velocity");
		debugVel.setData(BallsHack.mc.player.getLerpedPos(tickCounter.getTickProgress(false)), BallsHack.mc.player.getVelocity(), new Color(255, 0, 255));
		if (debugVel.getEnabled())
			debugVel.render3d(Renderer.getInstance(), matrixStack);

		PlayerSimDebugRenderer debugPredict = (PlayerSimDebugRenderer) DebugRenderers.getRenderer("playersim");
		if (debugPredict.getEnabled())
			debugPredict.render3d(Renderer.getInstance(), matrixStack);

		ScaffoldDebugRenderer debugScaffold = (ScaffoldDebugRenderer) DebugRenderers.getRenderer("scaffold");
		if (debugScaffold.getEnabled()) {
			debugScaffold.render3d(Renderer.getInstance(), matrixStack);
		}
		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
