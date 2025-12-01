package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {
//	@Accessor("vertexConsumers")
//	VertexConsumerProvider.Immediate getVertexConsumers();
}
