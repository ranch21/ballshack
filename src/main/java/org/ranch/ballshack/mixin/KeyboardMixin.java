package org.ranch.ballshack.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.event.events.EventKeyPress;
import org.ranch.ballshack.module.ModuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.ranch.ballshack.BallsHack.mc;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At(value = "TAIL"))
	public void onKey(long window, int action, KeyInput input, CallbackInfo ci) {

		if (action == GLFW.GLFW_PRESS && mc.currentScreen == null) {
			ModuleManager.handleKeyPress(input);
			BallsHack.eventBus.post(new EventKeyPress(input));
		}

		if (action == GLFW.GLFW_PRESS && input.getKeycode() == CommandManager.prefix.getValue() && mc.world != null) {
			mc.setScreen(new ChatScreen("", true));
		}
	}
}