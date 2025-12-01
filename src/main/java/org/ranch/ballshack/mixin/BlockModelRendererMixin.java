package org.ranch.ballshack.mixin;

import net.minecraft.client.render.block.BlockModelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {

	/*@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/model/BakedModel;FFFII)V", at = @At("HEAD"), cancellable = true)
	private void render(MatrixStack.Entry entry, VertexConsumer vertexConsumer, BlockState state, BakedModel bakedModel, float red, float green, float blue, int light, int overlay, CallbackInfo ci) {
		ci.cancel();
	}*/
}
