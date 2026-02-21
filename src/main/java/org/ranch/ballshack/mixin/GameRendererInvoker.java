package org.ranch.ballshack.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererInvoker {

	@Invoker("getFov")
	float ballshack$invokeGetFov(Camera camera, float tickProgress, boolean changingFov);
}
