package org.ranch.ballshack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.debug.renderers.VecDebugRenderer;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.util.rendering.Renderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(
			method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
					shift = At.Shift.AFTER
			),
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

		VecDebugRenderer debugVel = (VecDebugRenderer) DebugRenderers.getRenderer("velocity");
		debugVel.setData(BallsHack.mc.player.getLerpedPos(tickCounter.getTickProgress(false)), BallsHack.mc.player.getVelocity(), new Color(255, 0, 255));

		BallsHack.eventBus.post(event);

		Renderer.getInstance().renderQueues(matrixStack);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V",
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private void onScreenRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local(name = "drawContext") DrawContext drawContext) {
		int mouseX = (int)BallsHack.mc.mouse.getScaledX(BallsHack.mc.getWindow());
		int mouseY = (int)BallsHack.mc.mouse.getScaledY(BallsHack.mc.getWindow());

		EventScreen.Render event = new EventScreen.Render(BallsHack.mc.currentScreen, drawContext, mouseX, mouseY, tickCounter.getDynamicDeltaTicks());
		BallsHack.eventBus.post(event);
	}
}
