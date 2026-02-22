package org.ranch.ballshack.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RawProjectionMatrix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

	@Accessor("worldProjectionMatrix")
	RawProjectionMatrix getWorldProjectionMatrix();
}
