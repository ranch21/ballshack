package org.ranch.ballshack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(
			at = @At(value = "FIELD",
					target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
					opcode = Opcodes.GETFIELD,
					ordinal = 0),
			method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
			cancellable = true
	)
	private void onRenderWorldHandRendering(RenderTickCounter tickCounter,
											CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f3,
											@Local(ordinal = 1) float tickDelta)
	{
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.multiplyPositionMatrix(matrix4f3);
		EventWorldRender.Post event = new EventWorldRender.Post(matrixStack, tickCounter);

		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
