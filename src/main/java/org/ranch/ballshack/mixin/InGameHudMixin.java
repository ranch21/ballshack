package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventHudRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Inject(method = "render", at = @At("TAIL"))
	private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		EventHudRender event = new EventHudRender(context, tickCounter.getTickProgress(true));
		BallsHack.eventBus.post(event);
	}
}
