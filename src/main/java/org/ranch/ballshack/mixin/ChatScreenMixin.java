package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import org.ranch.ballshack.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
	@Inject(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addToMessageHistory(Ljava/lang/String;)V", ordinal = 0), cancellable = true)
	public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
		if (chatText.startsWith(CommandManager.prefix)) {
			CommandManager.onCommand(chatText.substring(1));
			ci.cancel();
		}
	}
}
