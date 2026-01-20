package org.ranch.ballshack.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.debug.renderers.ScaffoldDebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

	@Inject(method = "interactBlock", at = @At(value = "HEAD"))
	public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		ScaffoldDebugRenderer debugScaffold = (ScaffoldDebugRenderer) DebugRenderers.getRenderer("scaffold");
		debugScaffold.setData(hitResult.getBlockPos(), hitResult, hitResult.getSide());
	}

}
