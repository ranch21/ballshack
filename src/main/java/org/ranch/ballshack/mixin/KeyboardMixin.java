package org.ranch.ballshack.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventKeyPress;
import org.ranch.ballshack.module.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At(value = "TAIL"))
	public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {

		MinecraftClient mc = MinecraftClient.getInstance();

		if (action == GLFW.GLFW_PRESS && (mc.currentScreen == null || !(mc.currentScreen.getFocused() instanceof TextFieldWidget))) {
			ModuleManager.handleKeyPress(key);
			BallsHack.eventBus.post(new EventKeyPress(key));
		}
	}
}